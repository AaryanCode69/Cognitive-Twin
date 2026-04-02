package com.example.cognitivetwin.order.dto.Request;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record OrderRequestDTO(UUID userId, List<OrderItemRequestDTO> orderItems) {
}
