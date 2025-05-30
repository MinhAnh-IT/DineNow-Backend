package com.vn.DineNow.payload.response.statistical.revenue;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MonthlyRevenueDetail {
    int month;
    int year;
    int orderCount;
    BigDecimal revenue;
}
