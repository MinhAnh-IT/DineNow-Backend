package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.FoodCategoryDTO;
import com.vn.DineNow.entities.FoodCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FoodCategoryMapper {
    @Mapping(target = "menuItems", ignore = true)
    FoodCategory toEntity(FoodCategoryDTO foodCategoryDTO);
    FoodCategoryDTO toDTO(FoodCategory foodCategory);
}
