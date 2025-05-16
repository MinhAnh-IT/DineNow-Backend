package com.vn.DineNow.payload.response.restaurantPayment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RestaurantPaymentResponse {
    String restaurantName;
    String address;
    BigDecimal amount;
    String note;
    LocalDate startDate;
    LocalDate endDate;
}
