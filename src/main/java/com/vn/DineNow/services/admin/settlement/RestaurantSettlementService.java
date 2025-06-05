package com.vn.DineNow.services.admin.settlement;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.restaurantPayment.RestaurantPaymentRequest;
import com.vn.DineNow.payload.response.restaurantPayment.RestaurantPaymentResponse;
import com.vn.DineNow.payload.response.settlement.RestaurantSettlementInfoDTO;
import com.vn.DineNow.payload.response.settlement.SettlementSummaryDTO;

import java.time.YearMonth;
import java.util.List;

public interface RestaurantSettlementService {
    List<RestaurantSettlementInfoDTO> findUnsettledRestaurantsForCurrentPeriod() throws CustomException;
    SettlementSummaryDTO getSettlementSummary(Long restaurantId) throws CustomException;
    RestaurantPaymentResponse createRestaurantPayment(RestaurantPaymentRequest request) throws CustomException;
    List<RestaurantPaymentResponse> findSettledRestaurantsInPeriod(YearMonth yearMonth, int periodIndex) throws CustomException;
}
