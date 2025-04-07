package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.OrderDTO;
import com.vn.DineNow.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderOrderItems", ignore = true)
    @Mapping(source = "reservation", target = "reservation.id")
    Order toEntity(OrderDTO orderDTO);

    @Mapping(source = "reservation.id", target = "reservation")
    OrderDTO toDTO(Order order);

}
