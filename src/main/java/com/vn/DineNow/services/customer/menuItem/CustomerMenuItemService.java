package com.vn.DineNow.services.customer.menuItem;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.menuItem.MenuItemResponseDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemSimpleResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerMenuItemService {
    List<MenuItemSimpleResponseDTO> GetAllSimpleMenuItemAvailableForRestaurant(long restaurantId) throws CustomException;
    Page<MenuItemSimpleResponseDTO> GetAllMenuAvailableTrue(int page, int size);
    MenuItemResponseDTO getMenuItemById(long menuId) throws CustomException;
}
