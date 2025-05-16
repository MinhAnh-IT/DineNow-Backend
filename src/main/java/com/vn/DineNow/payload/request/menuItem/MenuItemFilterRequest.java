package com.vn.DineNow.payload.request.menuItem;

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
public class MenuItemFilterRequest {
    String city;
    String district;
    Long restaurantTypeId;
    BigDecimal minPrice;
    BigDecimal maxPrice;
    Long mainCategoryId;
}
