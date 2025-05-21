package com.vn.DineNow.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.APIResponse;
import com.vn.DineNow.services.customer.vnpay.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentController {

    final VnPayService vnPayService;

    @Value("${DineNow.redirect-url}")
    String paymentResultUrl;


    @PostMapping("/create-url/{orderId}")
    public ResponseEntity<?> createVNPayUrl(@PathVariable Long orderId, HttpServletRequest request) throws CustomException, JsonProcessingException {
        var result = vnPayService.createPaymentUrl(orderId, request);
        APIResponse<?> response = APIResponse.builder()
                .status(StatusCode.CREATED.getCode())
                .message(StatusCode.CREATED.getMessage())
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/vnpay/callback")
    public void handleVNPayCallback(@RequestParam Map<String, String> allParams,
                                    HttpServletResponse response) throws IOException, CustomException {
        boolean isSuccess = vnPayService.processCallback(allParams);

        String redirectUrl = String.format("%s?paymentStatus=%s", paymentResultUrl, isSuccess ? "SUCCESS" : "FAILED");
        log.info("VNPay callback: status={}, redirecting to {}", isSuccess ? "SUCCESS" : "FAILED", redirectUrl);
        response.sendRedirect(redirectUrl);
    }
}
