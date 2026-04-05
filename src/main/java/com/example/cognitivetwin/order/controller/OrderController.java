package com.example.cognitivetwin.order.controller;

import com.example.cognitivetwin.order.dto.Request.OrderRequestDTO;
import com.example.cognitivetwin.order.dto.Response.OrderResponseDTO;
import com.example.cognitivetwin.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO request){
        log.info("Order creation request received");
        OrderResponseDTO response = orderService.createOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponseDTO>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ){
        log.info("Received request to get orders with pagination - page: {}, size: {}", page, size);
        Page<OrderResponseDTO> response = orderService.getOrders(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable UUID id){
        log.info("Received request to get order with ID: {}", id);
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/users/{id}/orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrderByUserId(@PathVariable UUID id){
        log.info("Received request to get order for user with ID: {}", id);
        return ResponseEntity.ok(orderService.getOrderByUserId(id));
    }

}
