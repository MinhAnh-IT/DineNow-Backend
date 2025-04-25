package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.RestaurantTypeDTO;
import com.vn.DineNow.entities.RestaurantType;
import com.vn.DineNow.payload.request.restaurantType.RestaurantTypeRequest;
import com.vn.DineNow.payload.response.RestaurantTypeResponse.RestaurantTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantTypeMapper {

    RestaurantTypeResponse toDTO(RestaurantType restaurantType);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurants", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    RestaurantType toEntity(RestaurantTypeRequest restaurantTypeDTO);
}