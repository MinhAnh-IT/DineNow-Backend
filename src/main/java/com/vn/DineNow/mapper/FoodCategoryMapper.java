package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.FoodCategoryDTO;
import com.vn.DineNow.entities.FoodCategory;
import com.vn.DineNow.payload.response.foodCategory.FoodCategoryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodCategoryMapper {
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "menuItems", ignore = true)
    FoodCategory toEntity(FoodCategoryDTO foodCategoryDTO);
    FoodCategoryResponseDTO toDTO(FoodCategory foodCategory);
}
