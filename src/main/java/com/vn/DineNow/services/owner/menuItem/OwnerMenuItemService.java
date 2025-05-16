package com.vn.DineNow.services.owner.menuItem;

import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.menuItem.MenuItemFilterRequest;
import com.vn.DineNow.payload.request.menuItem.MenuItemRequestDTO;
import com.vn.DineNow.payload.request.menuItem.MenuItemUpdateDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemResponseDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemSimpleResponseDTO;

import java.util.List;

public interface OwnerMenuItemService {
    MenuItemResponseDTO addNewMenuItem(long owner, long restaurantId, MenuItemRequestDTO menuItemRequestDTO)
            throws CustomException;
    MenuItemResponseDTO updateMenuItem(long ownerId, long menuItemId, MenuItemUpdateDTO menuItemUpdateDTO) throws CustomException;
    boolean deleteMenuItem(long ownerId, long menuItemId) throws CustomException;
    List<MenuItemResponseDTO> GetAllMenuItemForRestaurant(long ownerId, long restaurantId) throws CustomException;
    boolean updateMenuItemAvailability(long ownerId, long menuItemId, boolean available) throws CustomException;
    void updateAvgRating(MenuItem menuItem) throws CustomException;
}
