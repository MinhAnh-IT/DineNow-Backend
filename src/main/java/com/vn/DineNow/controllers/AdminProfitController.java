package com.vn.DineNow.controllers;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.services.admin.statistical.AdminProfitService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/admin/profits")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminProfitController {

    AdminProfitService adminProfitService;

    @GetMapping("/restaurants/monthly")
    public ResponseEntity<?> getMonthlyProfitInYear(
            @RequestParam(value = "yearMonth", required = false) YearMonth month) {
        var result = adminProfitService.getMonthlyProfitsOfAllRestaurants(month);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurants/total")
    public ResponseEntity<?> getTotalProfitPerRestaurantSinceJoined() {
        var result = adminProfitService.getTotalProfitsPerRestaurant();
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurants/monthly/range")
    public ResponseEntity<?> getMonthlyProfitInRange(
            @RequestParam(value = "startMonth") YearMonth startMonth,
            @RequestParam(value = "endMonth") YearMonth endMonth) throws CustomException {
        var result = adminProfitService.getTotalProfitsPerRestaurantInRange(startMonth, endMonth);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
