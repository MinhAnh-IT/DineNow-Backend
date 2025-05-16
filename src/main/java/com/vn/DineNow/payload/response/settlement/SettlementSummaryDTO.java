package com.vn.DineNow.payload.response.settlement;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SettlementSummaryDTO {
    Long restaurantId;
    String restaurantName;

    String bankName;
    String accountHolderName;
    String accountNumber;

    Long totalOrders;
    BigDecimal totalRevenue;
    BigDecimal platformFee;
    BigDecimal amountToSettle;
}
