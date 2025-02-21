package com.project.order.service;

import com.project.order.models.Order;
import com.project.order.enums.*;
import com.project.order.respository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService
{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderQueue orderQueue;

    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);
        orderQueue.addOrder(savedOrder);
        return savedOrder;
    }

    public Order getOrderStatus(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
        System.out.println("All orders have been deleted");
    }

    public List<Order> getAllOrders() {
        List<Order>response= orderRepository.findAll();
        return response;
        
    }

    @Scheduled(fixedRate = 10)
    public void processOrders() {
        if (!orderQueue.isEmpty()) {
            Order order = orderQueue.getNextOrder();
            processOrderAsync(order);
        }
    }

    @Async("asyncExecutor")
    public void processOrderAsync(Order order) {
            order.setStatus(OrderStatus.PROCESSING);
            orderRepository.save(order);
            order.setStatus(OrderStatus.COMPLETED);
            order.setProcessedAt(LocalDateTime.now());
            orderRepository.save(order);
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalOrders", orderRepository.count());
        metrics.put("averageProcessingTime", calculateAverageProcessingTime());
        metrics.put("orderStatusCounts", getOrderStatusCounts());
        return metrics;
    }

    private double calculateAverageProcessingTime() {
        List<Order> completedOrders = orderRepository.findByStatus(OrderStatus.COMPLETED);
        if (completedOrders.isEmpty()) {
            return 0;
        }
        long totalProcessingTime = 0;
        for (Order order : completedOrders) {
            long processingTime = ChronoUnit.SECONDS.between(order.getCreatedAt(), order.getProcessedAt());
            totalProcessingTime += processingTime;
        }
    
        return (double) totalProcessingTime / completedOrders.size();
    }
    

    private Map<OrderStatus, Long> getOrderStatusCounts() {
        Map<OrderStatus, Long> statusCounts = new HashMap<>();
        for (OrderStatus status : OrderStatus.values()) {
            long count = orderRepository.findByStatus(status).size();
            statusCounts.put(status, count);
        }
        return statusCounts;
    }
    
    

    

    
}
