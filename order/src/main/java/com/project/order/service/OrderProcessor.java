package com.project.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import com.project.order.models.*;
import com.project.order.enums.*;
import jakarta.annotation.PostConstruct;

public class OrderProcessor
{
    @Autowired
    private OrderQueue orderQueue;

    @Autowired
    private OrderService orderService;

    @PostConstruct
    public void init() {
        processOrders();
    }

    @Async
    public void processOrders() {
        while (true) {
            try {
                Order order = orderQueue.getNextOrder();
                orderService.updateOrderStatus(order.getOrderId(), OrderStatus.PROCESSING);
                // Simulate processing time
                Thread.sleep(500); // 5 seconds
                orderService.updateOrderStatus(order.getOrderId(), OrderStatus.COMPLETED);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
}
