package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.MenuItemDTO;
import com.vn.DineNow.entities.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {

    @Mapping(target = "menuItemOrderItems", ignore = true)
    @Mapping(source = "restaurant", target = "restaurant.id")
    MenuItem toEntity(MenuItemDTO menuItemDTO);

    @Mapping(source = "restaurant.id", target = "restaurant")
    MenuItemDTO toDTO(MenuItem menuItem);

}
