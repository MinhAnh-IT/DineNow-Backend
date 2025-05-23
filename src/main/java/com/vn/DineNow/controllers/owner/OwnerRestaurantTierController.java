package com.vn.DineNow.controllers.owner;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.services.owner.restaurantTier.OwnerRestaurantTierService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/owner/restaurant-tiers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OwnerRestaurantTierController {
    OwnerRestaurantTierService ownerRestaurantTierService;

    @GetMapping()
    public ResponseEntity<?> getAllRestaurantTiers() {
        var result = ownerRestaurantTierService.getAllRestaurantTiers();
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
