package com.project.order.models;
import lombok.Data;
import java.math.BigDecimal;
import jakarta.persistence.*;
import com.project.order.enums.*;
@Data
@Entity
@Table(name = "orders")
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String orderId;

    private String userId;

    @Column(columnDefinition = "JSON")
    private String itemIds;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
}


