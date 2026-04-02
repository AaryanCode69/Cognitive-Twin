package com.example.cognitivetwin.order.service;


import com.example.cognitivetwin.exception.custom.ResourceNotFoundException;
import com.example.cognitivetwin.mapper.OrderMapper;
import com.example.cognitivetwin.order.OrderStatus;
import com.example.cognitivetwin.order.dto.Request.OrderItemRequestDTO;
import com.example.cognitivetwin.order.dto.Request.OrderRequestDTO;
import com.example.cognitivetwin.order.dto.Response.OrderItemResponseDTO;
import com.example.cognitivetwin.order.dto.Response.OrderResponseDTO;
import com.example.cognitivetwin.order.entity.OrderEntity;
import com.example.cognitivetwin.order.entity.OrderItem;
import com.example.cognitivetwin.order.repository.OrderRepository;
import com.example.cognitivetwin.user.entity.UserEntity;
import com.example.cognitivetwin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final OrderMapper orderMapper;

    @Transactional()
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest){

        log.info("Creating order request received");

        UserEntity user = userRepository.findById(orderRequest.userId()).orElseThrow(()->new ResourceNotFoundException("User not found"));

        if (orderRequest.orderItems() == null || orderRequest.orderItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        List<OrderItem> orderItems = orderRequest.orderItems().stream().map(orderMapper::mapOrderItemRequestToOrderItem).toList();

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUser(user);
        orderEntity.setOrderStatus(OrderStatus.CREATED);

        for(OrderItem item : orderItems){
            orderEntity.addOrderItem(item);
        }
        orderEntity.calculateTotal();


        orderEntity =  orderRepository.save(orderEntity);

        List<OrderItemResponseDTO> responseOrderItems = orderEntity.getOrderItems().stream().map(orderMapper::mapOrderItemtoOrderItemResponse).toList();

        log.info("Order Created Successfully" );
        return OrderResponseDTO.builder()
                .orderId(orderEntity.getId())
                .orderItems(responseOrderItems)
                .totalAmount(orderEntity.getTotalAmount())
                .orderStatus(orderEntity.getOrderStatus())
                .build();
    }
}
