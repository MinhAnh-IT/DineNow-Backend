package com.vn.DineNow.controllers.owner;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.payload.response.statistical.dashboard.OwnerDashboardResponseDTO;
import com.vn.DineNow.security.CustomUserDetails;
import com.vn.DineNow.services.owner.statistical.OwnerStatisticalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/owner/dashboard")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OwnerDashboardController {
    OwnerStatisticalService ownerStatisticalService;

    @GetMapping()
    public ResponseEntity<APIResponse<OwnerDashboardResponseDTO>> getDashboardData(
            @AuthenticationPrincipal CustomUserDetails userDetails) throws CustomException {
        var result = ownerStatisticalService.getDashboardData(userDetails.getId());
        APIResponse<OwnerDashboardResponseDTO> response = APIResponse.<OwnerDashboardResponseDTO>builder()
                .status(StatusCode.OK.getCode())
                .message(StatusCode.OK.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }
}
