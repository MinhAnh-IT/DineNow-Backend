package com.vn.DineNow.payload.response.statistical.revenue;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class YearlyRevenueResponse {
    private BigDecimal totalRevenue;
    private List<YearlyRevenueDetail> yearlyDetails;
}
