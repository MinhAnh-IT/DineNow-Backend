package com.vn.DineNow.payload.response.restaurant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantSimpleResponseForAdmin {
    Long id;
    String name;
    String address;
    String restaurantTierName;
    String thumbnailUrl;
    String typeName;
    OffsetDateTime createdAt;
    OffsetDateTime updatedAt;
}
