package com.example.cognitivetwin.order.service;


import com.example.cognitivetwin.exception.custom.ResourceNotFoundException;
import com.example.cognitivetwin.mapper.OrderMapper;
import com.example.cognitivetwin.order.OrderStatus;
import com.example.cognitivetwin.order.dto.OrderFilterDTO;
import com.example.cognitivetwin.order.dto.Request.OrderItemRequestDTO;
import com.example.cognitivetwin.order.dto.Request.OrderRequestDTO;
import com.example.cognitivetwin.order.dto.Response.OrderItemResponseDTO;
import com.example.cognitivetwin.order.dto.Response.OrderResponseDTO;
import com.example.cognitivetwin.order.entity.OrderEntity;
import com.example.cognitivetwin.order.entity.OrderItem;
import com.example.cognitivetwin.order.repository.OrderRepository;
import com.example.cognitivetwin.specifications.OrderSpecification;
import com.example.cognitivetwin.user.entity.UserEntity;
import com.example.cognitivetwin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private final OrderMapper orderMapper;

    private static final Set<String> allowedSortFields = Set.of("createdAt", "totalAmount", "orderStatus");


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

    public Page<OrderResponseDTO> getOrders(OrderFilterDTO orderFilterDTO, Pageable pageable) {
        log.info("Fetching orders with pagination - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        if(!allowedSortFields.contains(pageable.getSort().toString())) {
            log.warn("Sorting not by allowed field: {}", pageable.getSort());
            throw new IllegalArgumentException("Sorting by " + pageable.getSort() + " is not allowed");
        }
        Specification<OrderEntity> specification = OrderSpecification.getSpecification(orderFilterDTO);
        Page<OrderEntity> orderPage = orderRepository.findAll(specification,pageable);
        return orderPage.map(orderMapper::mapOrderEntityToOrderResponse);
    }

    public OrderResponseDTO getOrderById(UUID id) {
        log.info("Fetching order with ID: {}", id);
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return orderMapper.mapOrderEntityToOrderResponse(orderEntity);
    }

}
