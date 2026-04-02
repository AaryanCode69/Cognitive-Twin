package com.example.cognitivetwin.order.dto.Request;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


import java.math.BigDecimal;

@Builder
public record OrderItemRequestDTO(@NotBlank String productName, @Min(1) @Max(99) Integer quantity, @DecimalMin("0.0")  BigDecimal price) {
}
