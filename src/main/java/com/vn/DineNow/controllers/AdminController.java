package com.vn.DineNow.controllers;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.restaurantType.RestaurantTypeRequest;
import com.vn.DineNow.payload.request.restaurantType.RestaurantTypeUpdateRequest;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.RestaurantTypeResponse.RestaurantTypeResponse;
import com.vn.DineNow.services.admin.restaurantType.RestaurantTypeService;
import com.vn.DineNow.services.admin.user.AdminUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/admin/")
@RestController
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    AdminUserService adminService;
    RestaurantTypeService restaurantTypeService;


    @GetMapping("users")
    public ResponseEntity<APIResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = adminService.getAllUsers();
        APIResponse<List<UserDTO>> response = APIResponse.<List<UserDTO>>builder()
                .status(StatusCode.OK.getCode())
                .message("Users retrieved successfully")
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("restaurant-types")
    public ResponseEntity<APIResponse<List<RestaurantTypeResponse>>> getAllRestaurantTypeResponse() throws CustomException {
        var result = restaurantTypeService.getAllRestaurantType();
        APIResponse<List<RestaurantTypeResponse>> response = APIResponse.<List<RestaurantTypeResponse>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("restaurant-types")
    public ResponseEntity<APIResponse<RestaurantTypeResponse>> createRestaurantType(
            @ModelAttribute @Valid RestaurantTypeRequest request) throws CustomException {
        var result = restaurantTypeService.createRestaurantType(request);
        APIResponse<RestaurantTypeResponse> response = APIResponse.<RestaurantTypeResponse>builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("restaurant-types/{restaurantTypeId}")
    public ResponseEntity<APIResponse<RestaurantTypeResponse>> updateRestaurantType(
            @PathVariable long restaurantTypeId, @ModelAttribute RestaurantTypeUpdateRequest request) throws CustomException{
        var result = restaurantTypeService.updateRestaurantType(restaurantTypeId, request);
        APIResponse<RestaurantTypeResponse> response = APIResponse.<RestaurantTypeResponse>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("restaurant-types/{restaurantTypeId}")
    public ResponseEntity<APIResponse<Boolean>> deleteRestaurantTypeById(@PathVariable long restaurantTypeId) throws CustomException{
        var result = restaurantTypeService.deleteRestaurantType(restaurantTypeId);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.DELETED.getCode())
                .message(StatusCode.DELETED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
