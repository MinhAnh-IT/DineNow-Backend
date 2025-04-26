package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.MainCategory;
import com.vn.DineNow.payload.request.mainCategory.MainCategoryRequest;
import com.vn.DineNow.payload.request.mainCategory.MainCategoryUpdateRequest;
import com.vn.DineNow.payload.response.mainCategory.MainCategoryResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MainCategoryMapper {

    MainCategoryResponse toDto(MainCategory mainCategory);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "foodCategories", ignore = true)
    MainCategory toEntity(MainCategoryRequest mainCategoryDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "foodCategories", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(MainCategoryUpdateRequest mainCategoryDTO, @MappingTarget MainCategory mainCategory);
}
