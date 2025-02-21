package com.project.order.service;

import com.project.order.models.Order;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
public class OrderQueue
{
    private final Queue<Order> queue = new LinkedList<>();

    public void addOrder(Order order) {
        queue.offer(order);
    }

    public Order getNextOrder() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
