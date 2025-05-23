package com.vn.DineNow.controllers.publicEnpoint;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.services.customer.review.MenuItemReviewService;
import com.vn.DineNow.services.customer.review.RestaurantReviewService;
import com.vn.DineNow.validation.ValidRestaurantApprovedValidator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/reviews")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    MenuItemReviewService menuItemReviewService;
    RestaurantReviewService restaurantReviewService;


    @GetMapping("/menu-items/{menuId}/all")
    public ResponseEntity<APIResponse<?>> getAllReviewsByMenuItemId(
            @PathVariable long menuId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Exception {
        var result = menuItemReviewService.getAllReviewsByMenuItemId(menuId, page, size);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
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
