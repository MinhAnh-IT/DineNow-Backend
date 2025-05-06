package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.OrderItem;
import com.vn.DineNow.payload.request.orderItem.OrderItemSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.nio.file.Path;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "menuItemName", expression = "java(orderItem.getMenuItem().getName())")
    @Mapping(target = "totalPrice", source = "price")
    @Mapping(target = "menuItemPrice", source = "menuItem.price")
    @Mapping(target = "menuItemImageUrl", expression = "java(mapThumbnail(orderItem.getMenuItem().getImageUrl()))")
    @Mapping(target = "menuItemId", source = "menuItem.id")
    OrderItemSimpleResponse toSimpleDTO(OrderItem orderItem);

    default String mapThumbnail(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String filename = Path.of(imageUrl).getFileName().toString();
            return "http://localhost:8080/uploads/" + filename;
        }
        return null;
    }

}
