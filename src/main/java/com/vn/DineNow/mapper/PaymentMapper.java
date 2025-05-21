package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.Payment;
import com.vn.DineNow.payload.request.payment.PaymentRequest;
import com.vn.DineNow.payload.response.payment.PaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "transactionId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "order.id", source = "orderId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Payment toEntity(PaymentRequest paymentRequest);

    PaymentResponse toDTO(Payment payment);
}
