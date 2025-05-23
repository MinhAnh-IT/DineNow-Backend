package com.vn.DineNow.controllers.admin;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.restaurantPayment.RestaurantPaymentRequest;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.services.admin.settlement.RestaurantSettlementService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("api/admin/settlement")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SettlementController {

    RestaurantSettlementService restaurantSettlementService;

    @GetMapping("/unsettled")
    public ResponseEntity<?> getUnsettledRestaurants() throws CustomException {
        var result = restaurantSettlementService.findUnsettledRestaurantsForCurrentPeriod();
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary/{restaurantId}")
    public ResponseEntity<?> getSettlementSummary(@PathVariable Long restaurantId) throws CustomException {
        var result = restaurantSettlementService.getSettlementSummary(restaurantId);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmRestaurantSettlement(@RequestBody RestaurantPaymentRequest request) throws CustomException {
        var result = restaurantSettlementService.createRestaurantPayment(request);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/settled")
    public ResponseEntity<?> getSettledRestaurants(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("periodIndex") int periodIndex) throws CustomException {

        var yearMonth = YearMonth.of(year, month);
        var result = restaurantSettlementService.findSettledRestaurantsInPeriod(yearMonth, periodIndex);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
