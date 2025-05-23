package com.vn.DineNow.controllers.admin;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.services.admin.statistical.AdminRevenueService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("api/admin/revenues")
public class RevenueController {
    AdminRevenueService adminRevenueService;

    @GetMapping("/restaurants/monthly")
    public ResponseEntity<?> getMonthlyRevenueInYear(
            @RequestParam(value = "month", required = false) YearMonth yearMonth) throws CustomException {
        var result = adminRevenueService.getMonthlyRevenue(yearMonth);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurants/total")
    public ResponseEntity<?> getTotalRevenuePerRestaurantSinceJoined() throws CustomException {
        var result = adminRevenueService.getTotalRevenuePerRestaurantSinceJoined();
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurants/monthly/range")
    public ResponseEntity<?> getMonthlyRevenueInRange(
            @RequestParam(value = "startMonth") YearMonth startMonth,
            @RequestParam(value = "endMonth") YearMonth endMonth) throws CustomException {
        var result = adminRevenueService.getMonthlyRevenueInRange(startMonth, endMonth);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }



}
