package com.vn.DineNow.payload.response.RestaurantTypeResponse;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantTypeResponse {
    String name;
    String description;
    String imageUrl;
}
