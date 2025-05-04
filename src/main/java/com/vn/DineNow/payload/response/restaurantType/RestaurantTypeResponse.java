package com.vn.DineNow.payload.response.restaurantType;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantTypeResponse {
    long id;
    String name;
    String description;
    String imageUrl;
}
