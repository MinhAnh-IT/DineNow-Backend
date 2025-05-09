package com.vn.DineNow.payload.projection;

import java.math.BigDecimal;

public interface YearlyRevenueDTO {
    Integer getYear();

    Integer getOrderCount();

    BigDecimal getRevenue();
}
