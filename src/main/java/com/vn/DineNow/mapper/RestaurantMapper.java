package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.RestaurantDTO;
import com.vn.DineNow.entities.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "restaurantReviews", ignore = true)
    @Mapping(target = "restaurantRestaurantImages", ignore = true)
    @Mapping(target = "restaurantReservations", ignore = true)
    @Mapping(target = "restaurantMenuItems", ignore = true)
    @Mapping(target = "restaurantFavoriteRestaurants", ignore = true)
    @Mapping(target = "restaurantDiningTables", ignore = true)
    @Mapping(source = "owner", target = "owner.id")
    Restaurant toEntity(RestaurantDTO restaurantDTO);

    @Mapping(source = "owner.id", target = "owner")
    RestaurantDTO toDTO(Restaurant restaurant);
}
