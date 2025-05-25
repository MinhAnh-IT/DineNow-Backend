package com.vn.DineNow.controllers.publicEnpoint;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.googleMaps.LocationRequest;
import com.vn.DineNow.payload.request.restaurant.SearchRestaurantDTO;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.menuItem.MenuItemSimpleResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.services.customer.menuItem.CustomerMenuItemService;
import com.vn.DineNow.services.customer.restaurant.CustomerRestaurantServiceImpl;
import com.vn.DineNow.validation.ValidRestaurantApprovedValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantController {

    CustomerRestaurantServiceImpl restaurantService;
    CustomerMenuItemService menuItemService;

    @GetMapping()
    public ResponseEntity<APIResponse<?>> getALLRestaurant(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var result = restaurantService.getAllRestaurantStatusApproved(page, size);
        APIResponse<?> response = APIResponse.builder()
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

    @PostMapping("/search")
    public ResponseEntity<APIResponse<?>> searchRestaurant(
            @RequestBody SearchRestaurantDTO request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws CustomException {
        var result = restaurantService.searchRestaurant(request, page, size);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{restaurantId}/menu")
    public ResponseEntity<APIResponse<List<MenuItemSimpleResponseDTO>>> addItemForRestaurant(
            @ValidRestaurantApprovedValidator @PathVariable long restaurantId) throws CustomException {
        var result = menuItemService.GetAllSimpleMenuItemAvailableForRestaurant(restaurantId);
        APIResponse<List<MenuItemSimpleResponseDTO>> response = APIResponse.<List<MenuItemSimpleResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{typeId}")
    public ResponseEntity<APIResponse<List<RestaurantSimpleResponseDTO>>> getAllRestaurantByTypeId(
            @PathVariable long typeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) throws CustomException {
        var result = restaurantService.getAllRestaurantByTypeId(typeId, page, size);
        APIResponse<List<RestaurantSimpleResponseDTO>> response = APIResponse.<List<RestaurantSimpleResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/featured")
    public ResponseEntity<APIResponse<List<RestaurantSimpleResponseDTO>>> getListOfFeaturedRestaurants() throws CustomException {
        var result = restaurantService.GetListOfFeaturedRestaurants();
        APIResponse<List<RestaurantSimpleResponseDTO>> response = APIResponse.<List<RestaurantSimpleResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nearby")
    public ResponseEntity<APIResponse<List<RestaurantSimpleResponseDTO>>> findNearbyRestaurants(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "10") double radius,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) throws Exception {
        LocationRequest locationRequest = new LocationRequest(lat, lng);
        var result = restaurantService.findRestaurantsWithinRadius(locationRequest, radius, page, size);

        APIResponse<List<RestaurantSimpleResponseDTO>> response = APIResponse.<List<RestaurantSimpleResponseDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
