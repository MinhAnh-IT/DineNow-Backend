package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.FoodCategory;
import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.payload.request.menuItem.MenuItemRequestDTO;
import com.vn.DineNow.payload.request.menuItem.MenuItemUpdateDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemResponseDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemSimpleResponseDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "menuItemOrderItems", ignore = true)
    @Mapping(source = "category", target = "category")
    MenuItem toEntity(MenuItemRequestDTO dto);

    MenuItemResponseDTO toDTO(MenuItem menuItem);

    @Mapping(target = "typeName", source = "category.name")
    MenuItemSimpleResponseDTO toSimpleDTO(MenuItem menuItem);



    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "menuItemOrderItems", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMenuItem(@MappingTarget MenuItem menuItem, MenuItemUpdateDTO dto);

    default FoodCategory mapCategory(Long id) {
        if (id == null) return null;
        FoodCategory c = new FoodCategory();
        c.setId(id);
        return c;
    }
}
