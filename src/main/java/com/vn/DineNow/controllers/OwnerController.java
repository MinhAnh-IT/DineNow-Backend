package com.vn.DineNow.controllers;

import com.vn.DineNow.annotation.RequireEnabledUser;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.menuItem.MenuItemRequestDTO;
import com.vn.DineNow.payload.request.menuItem.MenuItemUpdateDTO;
import com.vn.DineNow.payload.request.restaurant.RestaurantRequestDTO;
import com.vn.DineNow.payload.request.restaurant.RestaurantUpdateDTO;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.menuItem.MenuItemResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.security.CustomUserDetails;
import com.vn.DineNow.services.menuItem.IMenuItemService;
import com.vn.DineNow.services.restaurant.IRestaurantService;
import com.vn.DineNow.validation.ValidRestaurantApprovedValidator;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Block;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
@Validated
public class OwnerController {
    private final IRestaurantService restaurantService;
    private final IMenuItemService menuItemService;

    @PostMapping("/restaurants")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<RestaurantResponseDTO>> createRestaurant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute @Valid RestaurantRequestDTO restaurantRequestDTO) throws CustomException{
        var result = restaurantService.createRestaurant(userDetails.getUser().getId(), restaurantRequestDTO);
        APIResponse<RestaurantResponseDTO> response = APIResponse.<RestaurantResponseDTO>builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/restaurants/{restaurantId}")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<RestaurantResponseDTO>> updateRestaurant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute RestaurantUpdateDTO requestDTO,
            @PathVariable @ValidRestaurantApprovedValidator long restaurantId) throws CustomException{
        var result = restaurantService.updateRestaurant(userDetails.getUser().getId(), restaurantId, requestDTO);
        APIResponse<RestaurantResponseDTO> response = APIResponse.<RestaurantResponseDTO>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/restaurants/{restaurantId}/menu")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<MenuItemResponseDTO>> addItemForRestaurant(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ValidRestaurantApprovedValidator @PathVariable long restaurantId,
            @ModelAttribute @Valid MenuItemRequestDTO menuItemRequestDTO) throws CustomException{
        var result = menuItemService.addNewMenuItem(userDetails.getUser().getId(), restaurantId, menuItemRequestDTO);
        APIResponse<MenuItemResponseDTO> response = APIResponse.<MenuItemResponseDTO>builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/menu-items/{id}")
    public ResponseEntity<APIResponse<MenuItemResponseDTO>> updateItem(
            @PathVariable long id, @ModelAttribute @RequestBody MenuItemUpdateDTO menuItemUpdateDTO) throws CustomException{
        var result = menuItemService.updateMenuItem(id, menuItemUpdateDTO);
        APIResponse<MenuItemResponseDTO> response = APIResponse.<MenuItemResponseDTO>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/menu-items/{id}")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<Boolean>> deleteMenuItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long id) throws CustomException{
        var result = menuItemService.deleteMenuItem(userDetails.getUser().getId(), id);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.DELETED.getCode())
                .message(StatusCode.DELETED.getMessage())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/menu-items/{restaurantId}")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<List<MenuItemResponseDTO>>> getAllMenuItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable @ValidRestaurantApprovedValidator long restaurantId
            ) throws CustomException{
        var result = menuItemService.GetAllMenuItemForRestaurant(userDetails.getUser().getId(), restaurantId);
        APIResponse<List<MenuItemResponseDTO>> response = APIResponse.<List<MenuItemResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("menu-items/{id}/available")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<Boolean>> updateAvailability(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Param("available") boolean available,
            @PathVariable long id) throws CustomException{
        var result = menuItemService.updateMenuItemAvailability(userDetails.getUser().getId(), id, available);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

}
