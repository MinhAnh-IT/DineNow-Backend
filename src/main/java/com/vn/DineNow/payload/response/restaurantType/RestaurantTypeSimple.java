package com.vn.DineNow.payload.response.restaurantType;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantTypeSimple {
    Long id;
    String name;
    String description;
}
