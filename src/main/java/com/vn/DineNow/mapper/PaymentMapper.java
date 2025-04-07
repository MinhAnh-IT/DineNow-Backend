package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.PaymentDTO;
import com.vn.DineNow.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "reservation", target = "reservation.id")
    Payment toEntity(PaymentDTO paymentDTO);

    @Mapping(source = "reservation.id", target = "reservation")
    PaymentDTO toDTO(Payment payment);
}
