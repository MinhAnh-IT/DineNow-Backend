package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.FoodCategory;
import com.vn.DineNow.payload.request.foodCategory.FoodCategoryRequest;
import com.vn.DineNow.payload.response.foodCategory.FoodCategoryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurant", expression = "java(mapRestaurant(request.getRestaurantId()))")
    @Mapping(target = "mainCategory", expression = "java(mapMainCategory(request.getMainCategoryId()))")
    @Mapping(target = "menuItems", ignore = true)
    FoodCategory toEntity(FoodCategoryRequest request);

    @Mapping(source = "restaurant.id", target = "restaurantId")
    @Mapping(source = "restaurant.name", target = "restaurantName")
    @Mapping(source = "mainCategory.id", target = "mainCategoryId")
    @Mapping(source = "mainCategory.name", target = "mainCategoryName")
    FoodCategoryResponseDTO toDTO(FoodCategory category);

    default com.vn.DineNow.entities.Restaurant mapRestaurant(Long id) {
        if (id == null) return null;
        var restaurant = new com.vn.DineNow.entities.Restaurant();
        restaurant.setId(id);
        return restaurant;
    }

    default com.vn.DineNow.entities.MainCategory mapMainCategory(Long id) {
        if (id == null) return null;
        var category = new com.vn.DineNow.entities.MainCategory();
        category.setId(id);
        return category;
    }
}
