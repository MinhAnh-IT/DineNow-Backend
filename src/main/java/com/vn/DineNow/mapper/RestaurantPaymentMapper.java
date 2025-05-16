package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.RestaurantPayment;
import com.vn.DineNow.payload.request.restaurantPayment.RestaurantPaymentRequest;
import com.vn.DineNow.payload.response.restaurantPayment.RestaurantPaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantPaymentMapper {
    @Mapping(target = "restaurant.id", source = "restaurantId")
    @Mapping(target = "paymentDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    RestaurantPayment toEntity(RestaurantPaymentRequest restaurantPaymentRequest);

    @Mapping(target = "restaurantName", source = "restaurant.name")
    @Mapping(target = "address", source = "restaurant.address")
    RestaurantPaymentResponse toResponse(RestaurantPayment restaurantPayment);
}
