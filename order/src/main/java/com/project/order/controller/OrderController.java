package com.project.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.order.service.OrderService;
import com.project.order.models.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController
{
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderStatus(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderStatus(orderId));
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllOrders() {
        orderService.deleteAllOrders();
        return ResponseEntity.ok("All orders have been deleted");
    }

    // @GetMapping("/metrics")
    // public ResponseEntity<Map<String, Object>> getMetrics() {
    //     return ResponseEntity.ok(orderService.getMetrics());
    // }
    
}
