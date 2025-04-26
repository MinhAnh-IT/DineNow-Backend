package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.FoodCategory;
import com.vn.DineNow.entities.MainCategory;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.payload.request.foodCategory.FoodCategoryRequest;
import com.vn.DineNow.payload.request.foodCategory.FoodCategoryUpdateRequest;
import com.vn.DineNow.payload.response.foodCategory.FoodCategoryResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface FoodCategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "mainCategory", expression = "java(mapMainCategory(request.getMainCategoryId()))")
    @Mapping(target = "menuItems", ignore = true)
    FoodCategory toEntity(FoodCategoryRequest request);

    @Mapping(source = "restaurant.name", target = "restaurantName")
    @Mapping(source = "mainCategory.name", target = "mainCategoryName")
    FoodCategoryResponseDTO toDTO(FoodCategory category);

    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "menuItems", ignore = true)
    @Mapping(target = "mainCategory", ignore = true)
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(FoodCategoryUpdateRequest request, @MappingTarget FoodCategory category);


    default Restaurant mapRestaurant(Long id) {
        if (id == null) return null;
        var restaurant = new com.vn.DineNow.entities.Restaurant();
        restaurant.setId(id);
        return restaurant;
    }

    default MainCategory mapMainCategory(Long id) {
        if (id == null) return null;
        var category = new com.vn.DineNow.entities.MainCategory();
        category.setId(id);
        return category;
    }
}
