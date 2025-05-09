package com.vn.DineNow.payload.response.statistical;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyRevenueResponse {
    BigDecimal totalRevenue;
    List<MonthlyRevenueDetail> monthlyDetails;
}
