package com.example.cognitivetwin.mapper;

import com.example.cognitivetwin.order.dto.Request.OrderItemRequestDTO;
import com.example.cognitivetwin.order.dto.Response.OrderItemResponseDTO;
import com.example.cognitivetwin.order.dto.Response.OrderResponseDTO;
import com.example.cognitivetwin.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderItem mapOrderItemRequestToOrderItem(OrderItemRequestDTO orderItemRequest);

    @Mapping(target = "subTotal",expression = "java(calculateSubTotal(orderItem.getPrice(),orderItem.getQuantity()))")
    OrderItemResponseDTO mapOrderItemtoOrderItemResponse(OrderItem orderItem);

    default BigDecimal calculateSubTotal(BigDecimal price, Integer quantity){
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
