package com.example.cognitivetwin.user.service;

import com.example.cognitivetwin.common.util.ValidateSortAttribute;
import com.example.cognitivetwin.exception.custom.ResourceNotFoundException;
import com.example.cognitivetwin.mapper.OrderMapper;
import com.example.cognitivetwin.mapper.UserMapper;
import com.example.cognitivetwin.exception.custom.EmailAlreadyExistsException;
import com.example.cognitivetwin.order.dto.Response.OrderResponseDTO;
import com.example.cognitivetwin.order.entity.OrderEntity;
import com.example.cognitivetwin.order.repository.OrderRepository;
import com.example.cognitivetwin.user.Role;
import com.example.cognitivetwin.user.dto.Request.UserRequestDTO;
import com.example.cognitivetwin.user.dto.Response.UserResponseDTO;
import com.example.cognitivetwin.user.entity.UserEntity;
import com.example.cognitivetwin.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final PasswordEncoder passwordEncoder;
    private final ValidateSortAttribute validateSortAttribute;

    @Transactional
    public UserResponseDTO registerUser(UserRequestDTO req){
        log.info("Registering New User");
        if (userRepository.existsByEmail(req.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        UserEntity userEntity = userMapper.mapUserRequestToUser(req);
        userEntity.setUserRole(Role.CUSTOMER);
        userEntity.setPassword(passwordEncoder.encode(req.password()));
        try{
            userEntity = userRepository.save(userEntity);
        }catch (DataIntegrityViolationException ex){
            log.warn("Duplicate email detected during save", ex);
            throw new EmailAlreadyExistsException("Email already exists");
        }
        log.info("User registration successful");
        return userMapper.mapUserToUserResponse(userEntity);
    }

    public UserEntity getUserById(UUID id){
        log.info("Fetching user with ID: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public Page<OrderResponseDTO> getUserOrders(UUID id, Pageable pageable) {
        log.info("Fetching all orders for user with ID: {}", id);
        UserEntity user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if(validateSortAttribute.validateSortAttribute(pageable.getSort().toString())) {
            log.warn("Sorting not by allowed field: {}", pageable.getSort());
            throw new IllegalArgumentException("Sorting by " + pageable.getSort() + " is not allowed");
        }
        Page<OrderEntity> orders = orderRepository.findByUser(user, pageable);
        return orders.map(orderMapper::mapOrderEntityToOrderResponse);
    }
}
