package com.example.cognitivetwin.order.dto.Request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record OrderRequestDTO(@NotNull UUID userId, @NotEmpty List<@Valid OrderItemRequestDTO> orderItems) {
}
