package com.example.cognitivetwin.payment.dto.Request;

import com.example.cognitivetwin.payment.PaymentMethod;

import jakarta.validation.constraints.NotNull;


import java.util.UUID;

public record PaymentRequestDTO(
        @NotNull UUID orderId,
        @NotNull PaymentMethod paymentMethod
) {
}
