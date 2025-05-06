package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.RestaurantImage;
import com.vn.DineNow.payload.request.restaurant.RestaurantRequestDTO;
import com.vn.DineNow.payload.request.restaurant.RestaurantUpdateDTO;
import com.vn.DineNow.payload.response.restaurant.FavoriteRestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseForAdmin;
import org.mapstruct.*;

import java.nio.file.Path;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mappings (
        {
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "foodCategories", ignore = true),
            @Mapping(target = "owner", ignore = true),
            @Mapping(target = "restaurantFavoriteRestaurants", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "restaurantMenuItems", ignore = true),
            @Mapping(target = "restaurantReservations", ignore = true),
            @Mapping(target = "restaurantRestaurantImages", ignore = true),
            @Mapping(target = "restaurantReviews", ignore = true),
            @Mapping(target = "type.id", source = "typeId"),
            @Mapping(target = "restaurantTier.id", source = "restaurantTierId") ,
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "averageRating", ignore = true)
        }
    )
    Restaurant toEntity(RestaurantRequestDTO requestDTO);


    @Mappings (
        {
            @Mapping(target = "imageUrls", ignore = true),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "restaurantTier.name", target = "restaurantTierName")
        }
    )
    RestaurantResponseDTO toDTO(Restaurant restaurant);


    @Mappings (
        {
            @Mapping(target = "typeName", source = "type.name"),
            @Mapping(target = "thumbnailUrl", expression =
                    "java(mapThumbnail(restaurant.getRestaurantRestaurantImages()))"),
            @Mapping(source = "restaurantTier.name", target = "restaurantTierName")
        }
    )
    FavoriteRestaurantResponseDTO toFavoriteRestaurantDTO(Restaurant restaurant);


    @Mappings(
        {
                @Mapping(source = "type.name", target = "typeName"),
                @Mapping(source = "restaurantTier.name", target = "restaurantTierName"),
                @Mapping(target = "thumbnailUrl", expression =
                        "java(mapThumbnail(restaurant.getRestaurantRestaurantImages()))"),
                @Mapping(target = "reservationCount", ignore = true),
        })
    RestaurantSimpleResponseDTO toSimpleDTO(Restaurant restaurant);

    @Mappings (
        {
            @Mapping(source = "type.name", target = "typeName"),
            @Mapping(source = "restaurantTier.name", target = "restaurantTierName"),
            @Mapping(target = "thumbnailUrl", expression =
                    "java(mapThumbnail(restaurant.getRestaurantRestaurantImages()))")
        }
    )
    RestaurantSimpleResponseForAdmin toSimpleDTOForAdmin(Restaurant restaurant);

    @Mappings (
        {
            @Mapping(target = "restaurantTier", ignore = true),
            @Mapping(target = "type", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "restaurantReviews", ignore = true),
            @Mapping(target = "restaurantRestaurantImages", ignore = true),
            @Mapping(target = "restaurantReservations", ignore = true),
            @Mapping(target = "restaurantMenuItems", ignore = true),
            @Mapping(target = "restaurantFavoriteRestaurants", ignore = true),
            @Mapping(target = "owner", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "foodCategories", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "averageRating", ignore = true)
        }
    )
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
