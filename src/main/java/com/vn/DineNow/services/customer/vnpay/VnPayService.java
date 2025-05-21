package com.vn.DineNow.services.customer.vnpay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vn.DineNow.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface VnPayService {
    String createPaymentUrl(Long orderId, HttpServletRequest httpRequest) throws CustomException, JsonProcessingException;
    boolean processCallback(Map<String, String> queryParams) throws CustomException;
}
