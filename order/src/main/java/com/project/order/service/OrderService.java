package com.project.order.service;

import com.project.order.models.Order;
import com.project.order.enums.*;
import com.project.order.respository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService
{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderQueue orderQueue;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderQueue orderQueue) {
        this.orderRepository = orderRepository;
        this.orderQueue = orderQueue;
    }

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

    @Scheduled(fixedRate = 1000)
    public void processOrders() {
        if (!orderQueue.isEmpty()) {
            Order order = orderQueue.getNextOrder();
            order.setStatus(OrderStatus.PROCESSING);
            orderRepository.save(order);
            
            // Simulate processing time
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            order.setStatus(OrderStatus.COMPLETED);
            order.setProcessedAt(LocalDateTime.now());
            orderRepository.save(order);
        }
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalOrders", orderRepository.count());
        metrics.put("averageProcessingTime", calculateAverageProcessingTime());
        return metrics;
    }

    private double calculateAverageProcessingTime() {
        List<Order> completedOrders = orderRepository.findByStatus(OrderStatus.COMPLETED);
        return completedOrders.stream()
                .mapToLong(order -> ChronoUnit.SECONDS.between(order.getCreatedAt(), order.getProcessedAt()))
                .average()
                .orElse(0);
    }

    public void deleteAllOrders() {
        orderRepository.deleteAll();
        System.out.println("All orders have been deleted");
    }

    public List<Order> getAllOrders() {
        List<Order>response= orderRepository.findAll();
        return response;
        
    }

    
}
