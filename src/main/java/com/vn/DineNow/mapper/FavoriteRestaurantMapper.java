package com.vn.DineNow.mapper;

import com.vn.DineNow.payload.request.favoriteRestaurant.FavoriteRestaurantRequest;
import com.vn.DineNow.entities.FavoriteRestaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteRestaurantMapper {

    @Mapping(source = "user", target = "user.id")
    @Mapping(source = "restaurant", target = "restaurant.id")
    FavoriteRestaurant toEntity(FavoriteRestaurantRequest favoriteRestaurantDTO);

    @Mapping(source = "user.id", target = "user")
    @Mapping(source = "restaurant.id", target = "restaurant")
    FavoriteRestaurantRequest toDTO(FavoriteRestaurant favoriteRestaurant);

}
