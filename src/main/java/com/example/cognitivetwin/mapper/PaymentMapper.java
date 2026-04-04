package com.example.cognitivetwin.mapper;

import com.example.cognitivetwin.payment.dto.Response.PaymentResponseDTO;
import com.example.cognitivetwin.payment.entity.PaymentEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentResponseDTO mapPaymentEntityToPaymentResponseDTO(PaymentEntity paymentEntity);
}
