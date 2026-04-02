package com.example.cognitivetwin.order.dto.Response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderItemResponseDTO(
        UUID id,
        String productName,
        Integer quantity,
        BigDecimal price,
        BigDecimal subTotal
) {}
