package com.vn.DineNow.payload.response.restaurant;

import com.vn.DineNow.enums.RestaurantStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantSimpleResponseDTO {
    Long id;
    String name;
    String address;
    Double averageRating;
    String restaurantTierName;
    RestaurantStatus status;
    String thumbnailUrl;
    String typeName;
    Long reservationCount;
}
