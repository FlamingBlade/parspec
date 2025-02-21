package com.project.order.service;

import com.project.order.models.Order;
import org.springframework.stereotype.Component;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class OrderQueue
{
    private final BlockingQueue<Order> queue = new LinkedBlockingQueue<>();

    public void addOrder(Order order) {
        queue.add(order);
    }

    public Order getNextOrder() throws InterruptedException {
        return queue.take();
    }

    public int getQueueSize() {
        return queue.size();
    }
}
