package com.project.order.service;

import com.project.order.models.Order;
import com.project.order.enums.*;
import com.project.order.respository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService
{
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderQueue orderQueue;


    @Transactional
    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);
        orderQueue.addOrder(savedOrder); // Add to processing queue
        return savedOrder;
    }

    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void deleteAllorders()
    {
        orderRepository.deleteAll();
    }

    @Transactional
    public void updateOrderStatus(String orderId, OrderStatus status) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
        });
    }
}
