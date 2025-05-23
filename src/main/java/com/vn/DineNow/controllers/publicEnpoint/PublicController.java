package com.vn.DineNow.controllers.publicEnpoint;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.restaurantType.RestaurantTypeResponse;
import com.vn.DineNow.services.admin.mainCategory.AdminMainCategoryService;
import com.vn.DineNow.services.admin.restaurantType.AdminRestaurantTypeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicController {
    AdminMainCategoryService mainCategoryService;
    AdminRestaurantTypeService restaurantTypeService;

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

    @GetMapping("main-categories")
    public ResponseEntity<APIResponse<List<?>>> getAllMainCategory() throws CustomException {
        var result = mainCategoryService.getAllMainCategories();
        APIResponse<List<?>> response = APIResponse.<List<?>>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
