package com.example.cognitivetwin.order.dto.Request;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderItemRequestDTO(String productName, Integer quantity, BigDecimal price) {
}
