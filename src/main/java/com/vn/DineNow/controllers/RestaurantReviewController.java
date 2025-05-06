package com.vn.DineNow.controllers;

import com.vn.DineNow.annotation.RequireEnabledUser;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.review.ReviewRequestDTO;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.security.CustomUserDetails;
import com.vn.DineNow.services.customer.review.RestaurantReviewService;
import com.vn.DineNow.validation.ValidRestaurantApprovedValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/reviews")
@RequiredArgsConstructor
@Validated
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantReviewController {
    RestaurantReviewService restaurantReviewService;

    @PostMapping("/restaurant/{restaurantId}/add")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<?>> addReview(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable @ValidRestaurantApprovedValidator long restaurantId,
            @RequestBody ReviewRequestDTO reviewDto) throws CustomException {
        var result = restaurantReviewService.addReview(userDetails.getId(), restaurantId, reviewDto);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurant/{restaurantId}/all")
    public ResponseEntity<APIResponse<?>> getAllReviewsByRestaurantId(
            @PathVariable @ValidRestaurantApprovedValidator long restaurantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws CustomException {
        var result = restaurantReviewService.getAllReviewsByRestaurantId(restaurantId, page, size);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
