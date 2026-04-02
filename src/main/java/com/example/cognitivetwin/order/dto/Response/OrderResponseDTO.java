package com.example.cognitivetwin.order.dto.Response;

import com.example.cognitivetwin.order.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder()
public record OrderResponseDTO(UUID orderId ,List<OrderItemResponseDTO> orderItems, OrderStatus orderStatus, BigDecimal totalAmount) {
}
