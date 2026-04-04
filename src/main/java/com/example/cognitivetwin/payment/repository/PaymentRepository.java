package com.example.cognitivetwin.payment.repository;

import com.example.cognitivetwin.order.entity.OrderEntity;
import com.example.cognitivetwin.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    boolean existsByOrder(OrderEntity orderEntity);
}
