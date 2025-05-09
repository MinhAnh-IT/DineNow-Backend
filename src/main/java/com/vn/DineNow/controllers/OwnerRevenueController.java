package com.vn.DineNow.controllers;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.security.CustomUserDetails;
import com.vn.DineNow.services.owner.statistical.OwnerStatisticalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.time.YearMonth;

@RestController
@RequestMapping("api/owner/revenue")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OwnerRevenueController {
    OwnerStatisticalService ownerStatisticalService;

    @GetMapping("restaurant/{restaurantId}/monthly")
    public ResponseEntity<?> getMonthlyRevenueInYear(
            @PathVariable long restaurantId,
            @RequestParam(value = "year", required = false) Integer year,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {

        int targetYear = (year != null) ? year : Year.now().getValue();

        var result = ownerStatisticalService.getMonthlyRevenueInYear(targetYear, userDetails.getId(), restaurantId);

        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("restaurant/{restaurantId}/yearly")
    public ResponseEntity<?> getYearlyRevenue(
            @PathVariable long restaurantId,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {
        var result = ownerStatisticalService.getYearlyRevenue(userDetails.getId(), restaurantId);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("restaurant/{restaurantId}/monthly/range")
    public ResponseEntity<?> getMonthlyRevenueInRange(
            @PathVariable long restaurantId,
            @RequestParam("startDate") YearMonth startDate,
            @RequestParam("endDate") YearMonth endDate,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {
        var result = ownerStatisticalService.getMonthlyRevenueInRange(startDate, endDate, userDetails.getId(), restaurantId);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

}
