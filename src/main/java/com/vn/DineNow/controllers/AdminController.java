package com.vn.DineNow.controllers;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.mainCategory.MainCategoryRequest;
import com.vn.DineNow.payload.request.mainCategory.MainCategoryUpdateRequest;
import com.vn.DineNow.payload.request.restaurantType.RestaurantTypeRequest;
import com.vn.DineNow.payload.request.restaurantType.RestaurantTypeUpdateRequest;
import com.vn.DineNow.payload.request.user.UserCreateRequest;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.RestaurantTypeResponse.RestaurantTypeResponse;
import com.vn.DineNow.payload.response.mainCategory.MainCategoryResponse;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseForAdmin;
import com.vn.DineNow.payload.response.user.UserDetailsResponse;
import com.vn.DineNow.payload.response.user.UserResponse;
import com.vn.DineNow.services.admin.mainCategory.AdminMainCategoryService;
import com.vn.DineNow.services.admin.restaurant.AdminRestaurantService;
import com.vn.DineNow.services.admin.restaurantType.AdminRestaurantTypeService;
import com.vn.DineNow.services.admin.user.AdminUserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
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
    AdminRestaurantTypeService restaurantTypeService;
    AdminMainCategoryService mainCategoryService;
    AdminRestaurantService adminRestaurantService;


    @GetMapping("users")
    public ResponseEntity<APIResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = adminService.getAllUsers();
        APIResponse<List<UserResponse>> response = APIResponse.<List<UserResponse>>builder()
                .status(StatusCode.OK.getCode())
                .message("Users retrieved successfully")
                .data(users)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("users/{userID}")
    public ResponseEntity<APIResponse<UserDetailsResponse>> getUserDetails(@PathVariable long userID) throws CustomException {
        var result = adminService.getUserDetails(userID);
        APIResponse<UserDetailsResponse> response = APIResponse.<UserDetailsResponse>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("users/owner")
    public ResponseEntity<APIResponse<UserResponse>> createOwner(@Valid @RequestBody UserCreateRequest request) throws CustomException {
        var result = adminService.CreateOwner(request);
        APIResponse<UserResponse> response = APIResponse.<UserResponse>builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
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

    @GetMapping("main-categories")
    public ResponseEntity<APIResponse<List<MainCategoryResponse>>> getAllMainCategories() throws CustomException {
        var result = mainCategoryService.getAllMainCategories();
        APIResponse<List<MainCategoryResponse>> response = APIResponse.<List<MainCategoryResponse>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("main-categories")
    public ResponseEntity<APIResponse<MainCategoryResponse>> createMainCategory(
            @Valid @RequestBody MainCategoryRequest mainCategoryDTO) throws CustomException {
        var result = mainCategoryService.createMainCategory(mainCategoryDTO);
        APIResponse<MainCategoryResponse> response = APIResponse.<MainCategoryResponse>builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("main-categories/{mainCategoryId}")
    public ResponseEntity<APIResponse<MainCategoryResponse>> updateMainCategory(
            @PathVariable Long mainCategoryId, @RequestBody MainCategoryUpdateRequest mainCategoryDTO) throws CustomException {
        var result = mainCategoryService.updateMainCategory(mainCategoryId, mainCategoryDTO);
        APIResponse<MainCategoryResponse> response = APIResponse.<MainCategoryResponse>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("main-categories/{mainCategoryId}")
    public ResponseEntity<APIResponse<Boolean>> deleteMainCategory(
            @PathVariable Long mainCategoryId) throws CustomException {
        var result = mainCategoryService.deleteMainCategory(mainCategoryId);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.DELETED.getCode())
                .message(StatusCode.DELETED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("restaurants/approve/{restaurantID}")
    public ResponseEntity<APIResponse<Boolean>> approveRestaurant(
            @PathVariable long restaurantID) throws CustomException {
        var result = adminRestaurantService.approveRestaurant(restaurantID);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("restaurants/reject/{restaurantID}")
    public ResponseEntity<APIResponse<Boolean>> rejectRestaurant(
            @PathVariable long restaurantID) throws CustomException {
        var result = adminRestaurantService.rejectRestaurant(restaurantID);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("restaurants/block/{restaurantID}")
    public ResponseEntity<APIResponse<Boolean>> blockRestaurant(
            @PathVariable long restaurantID) throws CustomException {
        var result = adminRestaurantService.blockRestaurant(restaurantID);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("restaurants/pending")
    public ResponseEntity<APIResponse<List<RestaurantSimpleResponseForAdmin>>> getAllPendingRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) throws CustomException {
        var result = adminRestaurantService.getAllPendingRestaurants(page, size);
        APIResponse<List<RestaurantSimpleResponseForAdmin>> response = APIResponse.
                <List<RestaurantSimpleResponseForAdmin>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("restaurants/approved")
    public ResponseEntity<APIResponse<List<RestaurantSimpleResponseForAdmin>>> getAllApprovedRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) throws CustomException {
        var result = adminRestaurantService.getAllApprovedRestaurants(page, size);
        APIResponse<List<RestaurantSimpleResponseForAdmin>> response = APIResponse.
                <List<RestaurantSimpleResponseForAdmin>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("restaurants/blocked")
    public ResponseEntity<APIResponse<List<RestaurantSimpleResponseForAdmin>>> getAllBlockedRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) throws CustomException {
        var result = adminRestaurantService.getAllBlockedRestaurants(page, size);
        APIResponse<List<RestaurantSimpleResponseForAdmin>> response = APIResponse.
                <List<RestaurantSimpleResponseForAdmin>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("restaurants/rejected")
    public ResponseEntity<APIResponse<List<RestaurantSimpleResponseForAdmin>>> getAllRejectedRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) throws CustomException {
        var result = adminRestaurantService.getAllRejectedRestaurants(page, size);
        APIResponse<List<RestaurantSimpleResponseForAdmin>> response = APIResponse.
                <List<RestaurantSimpleResponseForAdmin>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("restaurants/unblock/{restaurantID}")
    public ResponseEntity<APIResponse<Boolean>> unblockRestaurant(
            @PathVariable long restaurantID) throws CustomException {
        var result = adminRestaurantService.unblockRestaurant(restaurantID);
        APIResponse<Boolean> response = APIResponse.<Boolean>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
