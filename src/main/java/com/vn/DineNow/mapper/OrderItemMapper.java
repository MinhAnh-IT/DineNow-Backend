package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.OrderItemDTO;
import com.vn.DineNow.entities.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "order", target = "order.id")
    @Mapping(source = "menuItem", target = "menuItem.id")
    OrderItem toEntity(OrderItemDTO orderItemDTO);
    @Mapping(source = "order.id", target = "order")
    @Mapping(source = "menuItem.id", target = "menuItem")
    OrderItemDTO toDTO(OrderItem orderItem);
}
