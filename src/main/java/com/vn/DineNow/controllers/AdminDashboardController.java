package com.vn.DineNow.controllers;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.statistical.dashboard.AdminDashboardResponseDTO;
import com.vn.DineNow.services.admin.statistical.AdminDashboardService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/dashboard")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminDashboardController {

    AdminDashboardService adminDashboardService;

    @GetMapping()
    public ResponseEntity<APIResponse<AdminDashboardResponseDTO>> getDashboardData() throws Exception {
        var result = adminDashboardService.getDashboardData();
        APIResponse<AdminDashboardResponseDTO> response = APIResponse.<AdminDashboardResponseDTO>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
