package com.vn.DineNow.controllers;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.menuItem.MenuItemResponseDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemSimpleResponseDTO;
import com.vn.DineNow.services.customer.menuItem.CustomerMenuItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/menu-items")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuItemController {
    CustomerMenuItemService menuItemService;

    @GetMapping()
    public ResponseEntity<APIResponse<Page<MenuItemSimpleResponseDTO>>> getAllSimpleMenuItem(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        var result = menuItemService.GetAllMenuAvailableTrue(page, size);
        APIResponse<Page<MenuItemSimpleResponseDTO>> response = APIResponse.<Page<MenuItemSimpleResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{menuItemId}")
    public ResponseEntity<APIResponse<MenuItemResponseDTO>> getMenuItemById(
            @PathVariable long menuItemId) throws CustomException{
        var result = menuItemService.getMenuItemById(menuItemId);
        APIResponse<MenuItemResponseDTO> response = APIResponse.<MenuItemResponseDTO>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/main-category/{mainCategoryId}")
    public ResponseEntity<APIResponse<?>> getAllMenuItemByMainCategory(
            @PathVariable long mainCategoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) throws CustomException{
        var result = menuItemService.GetAllMenuItemAvailableTrueByMainCategory(mainCategoryId, page, size);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<APIResponse<?>> getAllMenuItemByCategory(
            @PathVariable long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) throws CustomException{
        var result = menuItemService.GetAllMenuItemAvailableTrueByCategory(categoryId, page, size);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/featured")
    public ResponseEntity<APIResponse<?>> getListMenuItemFeatured() throws CustomException{
        var result = menuItemService.getListMenuItemFeatured();
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
