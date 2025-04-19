package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.RestaurantDTO;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.RestaurantImage;
import com.vn.DineNow.payload.response.restaurant.FavoriteRestaurantResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "foodCategories", ignore = true)
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


    @Mapping(target = "thumbnailUrl", expression = "java(mapThumbnail(restaurant.getRestaurantRestaurantImages()))")
    @Mapping(source = "type", target = "type")
    FavoriteRestaurantResponseDTO toFavoriteRestaurantDTO(Restaurant restaurant);

    default String mapThumbnail(Set<RestaurantImage> images) {
        if (images != null && !images.isEmpty()) {
            return images.iterator().next().getImageUrl(); // take first image as thumbnail
        }
        return null;
    }


}
