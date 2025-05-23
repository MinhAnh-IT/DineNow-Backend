package com.vn.DineNow.controllers.customer;

import com.vn.DineNow.annotation.RequireEnabledUser;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.payload.request.review.ReviewRequestDTO;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.security.CustomUserDetails;
import com.vn.DineNow.services.customer.review.MenuItemReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/customer/reviews/menu-items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuItemReviewController {

    MenuItemReviewService menuItemReviewService;

    @PostMapping("{menuId}/add")
    @RequireEnabledUser
    public ResponseEntity<APIResponse<?>> addReview(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable long menuId,
            @RequestBody ReviewRequestDTO reviewDto) throws Exception {
        var result = menuItemReviewService.addReview(userDetails.getId(), menuId,reviewDto);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
