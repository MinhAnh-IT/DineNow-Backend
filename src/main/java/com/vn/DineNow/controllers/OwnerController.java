package com.vn.DineNow.controllers;

import com.vn.DineNow.annotation.RequireEnabledUser;
import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.foodCategory.FoodCategoryRequest;
import com.vn.DineNow.payload.request.foodCategory.FoodCategoryUpdateRequest;
import com.vn.DineNow.payload.request.menuItem.MenuItemRequestDTO;
import com.vn.DineNow.payload.request.menuItem.MenuItemUpdateDTO;
import com.vn.DineNow.payload.request.restaurant.RestaurantRequestDTO;
import com.vn.DineNow.payload.request.restaurant.RestaurantUpdateDTO;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.foodCategory.FoodCategoryResponseDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemResponseDTO;
import com.vn.DineNow.payload.response.order.OrderResponse;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.security.CustomUserDetails;
import com.vn.DineNow.services.customer.order.CustomerOrderService;
import com.vn.DineNow.services.owner.foodCategory.FoodCategoryService;
import com.vn.DineNow.services.owner.menuItem.OwnerMenuItemService;
import com.vn.DineNow.services.owner.order.OwnerOrderService;
import com.vn.DineNow.services.owner.restaurant.OwnerRestaurantService;
import com.vn.DineNow.validation.ValidRestaurantApprovedValidator;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/owner")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OwnerController {
    OwnerRestaurantService restaurantService;
    OwnerMenuItemService menuItemService;
    FoodCategoryService foodCategoryService;
    CustomerOrderService customerOrderService;
    OwnerOrderService ownerOrderService;

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
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long id,
            @ModelAttribute @RequestBody MenuItemUpdateDTO menuItemUpdateDTO) throws CustomException{
        var result = menuItemService.updateMenuItem(userDetails.getUser().getId(), id, menuItemUpdateDTO);
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

    @PutMapping("/menu-items/{id}/available")
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

    @GetMapping("/restaurants")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<List<RestaurantSimpleResponseDTO>>> getRestaurantByOwner(
            @AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException{
        var result = restaurantService.getRestaurantByOwner(userDetails.getUser().getId());

        APIResponse<List<RestaurantSimpleResponseDTO>> response = APIResponse.<List<RestaurantSimpleResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/food-categories/{restaurantId}")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<List<FoodCategoryResponseDTO>>> getAllFoodCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable @ValidRestaurantApprovedValidator long restaurantId) throws CustomException{
        var result = foodCategoryService.getAllFoodCategoryForRestaurant(userDetails.getUser().getId(), restaurantId);
        APIResponse<List<FoodCategoryResponseDTO>> response = APIResponse.<List<FoodCategoryResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/food-categories/{restaurantId}")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<FoodCategoryResponseDTO>> addNewFoodCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable @ValidRestaurantApprovedValidator long restaurantId,
            @RequestBody FoodCategoryRequest request) throws CustomException{
        var result = foodCategoryService.addNewFoodCategory(userDetails.getUser().getId(), restaurantId, request);
        APIResponse<FoodCategoryResponseDTO> response = APIResponse.<FoodCategoryResponseDTO>builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/food-categories/{foodCategoryId}")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<FoodCategoryResponseDTO>> updateFoodCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long foodCategoryId,
            @RequestBody FoodCategoryUpdateRequest request) throws CustomException{
        var result = foodCategoryService.updateFoodCategory(userDetails.getUser().getId(), foodCategoryId, request);
        APIResponse<FoodCategoryResponseDTO> response = APIResponse.<FoodCategoryResponseDTO>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/food-categories/{foodCategoryId}")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<Boolean>> deleteFoodCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long foodCategoryId) throws CustomException{
        var result = foodCategoryService.deleteFoodCategory(userDetails.getUser().getId(), foodCategoryId);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.DELETED.getCode())
                .message(StatusCode.DELETED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }


    @GetMapping("/orders/{orderId}")
    public ResponseEntity<APIResponse<?>> getOrderDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long orderId) throws Exception {
        var result = ownerOrderService.getOrderDetail(orderId, userDetails.getId());
        APIResponse<?> response = APIResponse.<Object>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurant/{restaurantId}/orders")
    public ResponseEntity<APIResponse<List<?>>> getAllOrderByStatuses(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long restaurantId,
            @RequestParam(name = "status") Set<OrderStatus> statuses) throws CustomException {
        var result = ownerOrderService.getAllOrderByStatuses(userDetails.getId(), restaurantId, statuses);
        APIResponse<List<?>> response = APIResponse.<List<?>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<APIResponse<Boolean>> updateOrderStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long orderId,
            @RequestBody OrderStatus status) throws Exception {
        var result = ownerOrderService.updateOrderStatus(userDetails.getId(), orderId, status);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
