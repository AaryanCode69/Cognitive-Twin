package com.example.cognitivetwin.order.entity;

import com.example.cognitivetwin.common.BaseEntity;
import com.example.cognitivetwin.order.OrderStatus;
import com.example.cognitivetwin.payment.entity.PaymentEntity;
import com.example.cognitivetwin.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders",indexes = @Index(
        name = "idx_orders_user_id",
        columnList = "user_id"
))
@Builder()
public class OrderEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus = OrderStatus.CREATED;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal totalAmount;

    @OneToOne(mappedBy = "order",cascade = CascadeType.ALL)
    private PaymentEntity payment;

    public void addOrderItem(OrderItem item) {
        orderItems.add(item);
        item.setOrder(this);
    }

    public void removeOrderItem(OrderItem item) {
        orderItems.remove(item);
        item.setOrder(null);
    }

    public void calculateTotal() {
        this.totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
