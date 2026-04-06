package com.example.cognitivetwin.user.controller;

import com.example.cognitivetwin.order.dto.Response.OrderResponseDTO;
import com.example.cognitivetwin.user.dto.Request.UserRequestDTO;
import com.example.cognitivetwin.user.dto.Response.UserResponseDTO;
import com.example.cognitivetwin.user.entity.UserEntity;
import com.example.cognitivetwin.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request){
        log.info("Received user registration request");
        UserResponseDTO userResponseDTO = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable UUID id){
        log.info("Received request to get user with ID: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<Page<OrderResponseDTO>> getUserOrders(@PathVariable UUID id,
                                                                @PageableDefault(size = 10,sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable){
        log.info("Received request to get orders for user with ID: {} ", id);
        Page<OrderResponseDTO> orders = userService.getUserOrders(id, pageable);
        return ResponseEntity.ok(orders);

    }
}
