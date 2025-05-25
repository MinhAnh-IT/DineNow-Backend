package com.vn.DineNow.payload.response.restaurant;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantSimple {
    long id;
    String name;
    String address;
    String thumbnailUrl;
    float averageRating;
}
