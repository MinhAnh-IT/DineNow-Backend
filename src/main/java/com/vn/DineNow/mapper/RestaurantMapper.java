package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.RestaurantImage;
import com.vn.DineNow.payload.request.restaurant.RestaurantRequestDTO;
import com.vn.DineNow.payload.request.restaurant.RestaurantUpdateDTO;
import com.vn.DineNow.payload.response.restaurant.FavoriteRestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import org.mapstruct.*;

import java.nio.file.Path;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(target = "commissionFee", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "foodCategories", ignore = true)
    @Mapping(target = "restaurantDiningTables", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "restaurantFavoriteRestaurants", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "restaurantMenuItems", ignore = true)
    @Mapping(target = "restaurantReservations", ignore = true)
    @Mapping(target = "restaurantRestaurantImages", ignore = true)
    @Mapping(target = "restaurantReviews", ignore = true)
    @Mapping(target = "type.id", source = "typeId")
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    Restaurant toEntity(RestaurantRequestDTO requestDTO);

    @Mapping(target = "imageUrls", ignore = true)
    @Mapping(source = "type", target = "type")
    RestaurantResponseDTO toDTO(Restaurant restaurant);


    @Mapping(target = "thumbnailUrl", expression = "java(mapThumbnail(restaurant.getRestaurantRestaurantImages()))")
    @Mapping(source = "type", target = "type")
    FavoriteRestaurantResponseDTO toFavoriteRestaurantDTO(Restaurant restaurant);

    @Mapping(source = "type.name", target = "typeName")
    @Mapping(target = "thumbnailUrl", expression = "java(mapThumbnail(restaurant.getRestaurantRestaurantImages()))")
    RestaurantSimpleResponseDTO toSimpleDTO(Restaurant restaurant);

    @Mapping(target = "commissionFee", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "restaurantReviews", ignore = true)
    @Mapping(target = "restaurantRestaurantImages", ignore = true)
    @Mapping(target = "restaurantReservations", ignore = true)
    @Mapping(target = "restaurantMenuItems", ignore = true)
    @Mapping(target = "restaurantFavoriteRestaurants", ignore = true)
    @Mapping(target = "restaurantDiningTables", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "foodCategories", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRestaurantFromRequest(RestaurantUpdateDTO dto, @MappingTarget Restaurant restaurant);


    default String mapThumbnail(Set<RestaurantImage> images) {
        if (images != null && !images.isEmpty()) {
            String filename = Path.of(images.iterator().next().getImageUrl()).getFileName().toString();
            return "http://localhost:8080/uploads/" + filename;
        }
        return null;
    }
}
