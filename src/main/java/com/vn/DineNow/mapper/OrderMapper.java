package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.Order;
import com.vn.DineNow.payload.request.Order.OrderRequest;
import com.vn.DineNow.payload.response.order.OrderDetailResponse;
import com.vn.DineNow.payload.response.order.OrderResponse;
import com.vn.DineNow.payload.response.order.OrderSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ReservationMapper.class, OrderItemMapper.class})
public interface OrderMapper {

    @Mappings({
            @Mapping(target = "orderOrderItems", ignore = true),
            @Mapping(target = "reservation", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "totalPrice", ignore = true),
            @Mapping(target = "updatedAt", ignore = true)
    })
    Order toEntity(OrderRequest orderDTO);

    @Mapping(target = "totalItems", expression = "java(order.getOrderOrderItems() != null ? order.getOrderOrderItems().size() : 0)")
    OrderResponse toDTO(Order order);

    @Mapping(source = "orderOrderItems", target = "menuItems")
    OrderDetailResponse toDetailDTO(Order order);

    @Mappings({
            @Mapping(source = "orderOrderItems", target = "menuItems"),
            @Mapping(source = "reservation", target = "reservationSimpleResponse")
    })
    OrderSimpleResponse toSimpleDTO(Order order);
}
