package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.RestaurantTypeDTO;
import com.vn.DineNow.entities.RestaurantType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantTypeMapper {

    RestaurantTypeDTO toDTO(RestaurantType restaurantType);

    @Mapping(target = "restaurants", ignore = true)
    RestaurantType toEntity(RestaurantTypeDTO restaurantTypeDTO);
}