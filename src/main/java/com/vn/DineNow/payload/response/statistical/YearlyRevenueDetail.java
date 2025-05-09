package com.vn.DineNow.payload.response.statistical;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class YearlyRevenueDetail {
    int year;
    int orderCount;
    BigDecimal revenue;
}
