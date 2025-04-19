package com.vn.DineNow.services.menuItem;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.menuItem.MenuItemRequestDTO;
import com.vn.DineNow.payload.request.menuItem.MenuItemUpdateDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemResponseDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemSimpleResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IMenuItemService {
    MenuItemResponseDTO addNewMenuItem(long owner, long restaurantId, MenuItemRequestDTO menuItemRequestDTO)
            throws CustomException;
    List<MenuItemSimpleResponseDTO> GetAllSimpleMenuItemAvailableForRestaurant(long restaurantId) throws CustomException;
    Page<MenuItemSimpleResponseDTO> GetAllMenuAvailableTrue(int page, int size);
    MenuItemResponseDTO updateMenuItem(long menuItemId, MenuItemUpdateDTO menuItemUpdateDTO) throws CustomException;
    boolean deleteMenuItem(long ownerId, long menuItemId) throws CustomException;
    MenuItemResponseDTO getMenuItemById(long menuId) throws CustomException;

    List<MenuItemResponseDTO> GetAllMenuItemForRestaurant(long ownerId, long restaurantId) throws CustomException;

    boolean updateMenuItemAvailability(long ownerId, long menuItemId, boolean available) throws CustomException;

}
