package com.vn.DineNow.controllers;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.restaurantTiers.RestaurantTieUpdateRequest;
import com.vn.DineNow.payload.request.restaurantTiers.RestaurantTiersRequest;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.services.admin.restaurantTier.RestaurantTierService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("api/admin/restaurant-tiers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantTierController {
    RestaurantTierService restaurantTierService;

    @PostMapping()
    public ResponseEntity<?> createRestaurantTier(@RequestBody @Valid RestaurantTiersRequest request) {
        var result = restaurantTierService.createRestaurantTier(request);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<?> getAllRestaurantTiers() {
        var result = restaurantTierService.getAllRestaurantTiers();
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRestaurantTier(@PathVariable long id, @RequestBody @Valid RestaurantTieUpdateRequest request) throws CustomException {
        var result = restaurantTierService.updateRestaurantTier(id, request);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
