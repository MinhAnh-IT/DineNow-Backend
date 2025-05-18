package com.vn.DineNow.payload.request.restaurantTiers;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantTieUpdateRequest {
    String name;
    BigDecimal feePerGuest;
    String description;
}
