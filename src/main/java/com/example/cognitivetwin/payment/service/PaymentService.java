package com.example.cognitivetwin.payment.service;

import com.example.cognitivetwin.exception.custom.ResourceNotFoundException;
import com.example.cognitivetwin.mapper.PaymentMapper;
import com.example.cognitivetwin.order.OrderStatus;
import com.example.cognitivetwin.order.entity.OrderEntity;
import com.example.cognitivetwin.order.repository.OrderRepository;
import com.example.cognitivetwin.payment.PaymentMethod;
import com.example.cognitivetwin.payment.PaymentStatus;
import com.example.cognitivetwin.payment.dto.Request.PaymentRequestDTO;
import com.example.cognitivetwin.payment.dto.Response.PaymentResponseDTO;
import com.example.cognitivetwin.payment.entity.PaymentEntity;
import com.example.cognitivetwin.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final OrderRepository orderRepository;

    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentResponseDTO createPayment(PaymentRequestDTO request){

        log.info("Processing payment for order");

        OrderEntity orderEntity = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + request.orderId()));

        if (paymentRepository.existsByOrder(orderEntity)) {
            throw new IllegalStateException("Payment already exists for this order");
        }

        if (orderEntity.getOrderStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException("Order is not eligible for payment");
        }

        PaymentEntity paymentEntity = PaymentEntity.builder()
                .order(orderEntity)
                .amount(orderEntity.getTotalAmount())
                .paymentMethod(request.paymentMethod())
                .paymentStatus(PaymentStatus.PENDING) // Simulate successful payment
                .transactionReference("TXN-" + UUID.randomUUID()) // Simulate transaction reference
                .build();

        orderEntity.setOrderStatus(OrderStatus.COMPLETED);


        paymentEntity = paymentRepository.save(paymentEntity);

        log.info("Payment successful");

        return paymentMapper.mapPaymentEntityToPaymentResponseDTO(paymentEntity);
    }

}
