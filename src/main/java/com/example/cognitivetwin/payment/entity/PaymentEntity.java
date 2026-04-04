package com.example.cognitivetwin.payment.entity;

import com.example.cognitivetwin.common.BaseEntity;
import com.example.cognitivetwin.order.entity.OrderEntity;
import com.example.cognitivetwin.payment.PaymentMethod;
import com.example.cognitivetwin.payment.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "payments",
        indexes = @Index(name = "idx_payment_order_id", columnList = "order_id")
)
@Builder
public class PaymentEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id",nullable = false,unique = true)
    private OrderEntity order;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal amount;

    @Column(unique = true,nullable = false)
    private String transactionReference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
}
