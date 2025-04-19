package com.vn.DineNow.controllers;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.restaurant.SearchRestaurantDTO;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.menuItem.MenuItemSimpleResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.services.menuItem.IMenuItemService;
import com.vn.DineNow.services.restaurant.RestaurantService;
import com.vn.DineNow.validation.ValidRestaurantApprovedValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantController {
    RestaurantService restaurantService;
    IMenuItemService menuItemService;

    @GetMapping()
    public ResponseEntity<APIResponse<Page<RestaurantSimpleResponseDTO>>> getALLRestaurant(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        var result = restaurantService.getAllRestaurant(page, size);
        APIResponse<Page<RestaurantSimpleResponseDTO>> response = APIResponse.<Page<RestaurantSimpleResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{restaurantID}")
    public ResponseEntity<APIResponse<RestaurantResponseDTO>> getRestaurantByID(
            @PathVariable @ValidRestaurantApprovedValidator long restaurantID) throws CustomException {
        var result = restaurantService.getRestaurantByID(restaurantID);
        APIResponse<RestaurantResponseDTO> response = APIResponse.<RestaurantResponseDTO>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse<Page<RestaurantSimpleResponseDTO>>> searchRestaurant(
            @RequestBody SearchRestaurantDTO request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws CustomException{
        var result = restaurantService.searchRestaurant(request, page, size);
        APIResponse<Page<RestaurantSimpleResponseDTO>> response = APIResponse.<Page<RestaurantSimpleResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<APIResponse<List<MenuItemSimpleResponseDTO>>> addItemForRestaurant(
            @ValidRestaurantApprovedValidator @PathVariable long restaurantId) throws CustomException{
        var result = menuItemService.GetAllSimpleMenuItemAvailableForRestaurant(restaurantId);
        APIResponse<List<MenuItemSimpleResponseDTO>> response = APIResponse.<List<MenuItemSimpleResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }



}
