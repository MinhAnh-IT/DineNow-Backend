package com.vn.DineNow.payload.request.restaurant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchRestaurantDTO {
    String province;
    String restaurantName;
}
