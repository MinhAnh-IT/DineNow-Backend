package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.RestaurantImageDTO;
import com.vn.DineNow.entities.RestaurantImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantImageMapper {
    @Mapping(source = "restaurant", target = "restaurant.id")
    RestaurantImage toEntity(RestaurantImageDTO restaurantImageDTO);

    @Mapping(source = "restaurant.id", target = "restaurant")
    RestaurantImageDTO toDTO(RestaurantImage restaurantImage);
}
