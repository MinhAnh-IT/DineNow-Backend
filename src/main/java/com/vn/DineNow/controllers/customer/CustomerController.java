package com.vn.DineNow.controllers.customer;

import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.payload.request.Order.OrderRequest;
import com.vn.DineNow.payload.request.googleMaps.LocationRequest;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.order.OrderSimpleResponse;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.security.CustomUserDetails;
import com.vn.DineNow.services.customer.order.CustomerOrderService;
import com.vn.DineNow.validation.ValidRestaurantApprovedValidator;
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
@RequiredArgsConstructor
@RequestMapping("/api/customers/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class CustomerController {
    CustomerOrderService customerOrderService;

    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<APIResponse<OrderSimpleResponse>> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable @ValidRestaurantApprovedValidator Long restaurantId,
            @RequestBody @Valid OrderRequest orderRequest) throws Exception {
        var result = customerOrderService.createOrder(
                userDetails.getId(),
                restaurantId,
                orderRequest);
        APIResponse<OrderSimpleResponse> response = APIResponse.<OrderSimpleResponse>builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("restaurant/{restaurantId}")
    public ResponseEntity<APIResponse<?>> getAllOrderByRestaurantId(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable @ValidRestaurantApprovedValidator Long restaurantId) throws Exception {
        var result = customerOrderService.getAllOrderByRestaurantAndCustomer(userDetails.getUser().getId(), restaurantId);
        APIResponse<?> response = APIResponse.<Object>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<APIResponse<?>> getAllOrderByCustomerId(
            @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {
        var result = customerOrderService.getAllOrderByCustomer(userDetails.getUser().getId());
        APIResponse<?> response = APIResponse.<Object>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<APIResponse<?>> getAllOrderByStatusPendingAndPaid(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "status") Set<OrderStatus> statuses) throws Exception {
        var result = customerOrderService.getAllOrderByStatusPendingAndPaid(userDetails.getUser().getId(), statuses);
        APIResponse<?> response = APIResponse.<Object>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<APIResponse<?>> cancelOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long orderId) throws Exception {
        var result = customerOrderService.cancelOrder(userDetails.getUser().getId(), orderId);
        APIResponse<?> response = APIResponse.<Object>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

}
