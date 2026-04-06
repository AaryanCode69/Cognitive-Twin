package com.example.cognitivetwin.order.dto;

import com.example.cognitivetwin.order.OrderStatus;
import com.example.cognitivetwin.payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderFilterDTO(OrderStatus orderStatus, UUID userId, Instant createdAfter, Instant createdBefore,
                             BigDecimal minTotalAmount, BigDecimal maxTotalAmount, PaymentStatus paymentStatus) {
}
