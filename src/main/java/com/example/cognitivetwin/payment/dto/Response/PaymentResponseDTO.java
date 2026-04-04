package com.example.cognitivetwin.payment.dto.Response;

import com.example.cognitivetwin.payment.PaymentMethod;
import com.example.cognitivetwin.payment.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentResponseDTO(UUID orderId, PaymentStatus paymentStatus, String transactionReference, PaymentMethod paymentMethod ,
                                 BigDecimal amount) {
}
