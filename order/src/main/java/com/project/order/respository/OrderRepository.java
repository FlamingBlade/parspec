package com.project.order.respository;
import com.project.order.enums.OrderStatus;
import com.project.order.models.Order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>
{
    List<Order> findByStatus(OrderStatus status);
}