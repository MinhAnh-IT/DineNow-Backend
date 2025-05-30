package com.vn.DineNow.payload.response.restaurantTiers;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class RestaurantTiersResponse {
    long id;
    String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal feePerGuest;
    String description;
}
