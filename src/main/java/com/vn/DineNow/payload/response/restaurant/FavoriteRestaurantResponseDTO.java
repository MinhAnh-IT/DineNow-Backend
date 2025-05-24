package com.vn.DineNow.payload.response.restaurant;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteRestaurantResponseDTO {
    Long id;
    String name;
    String address;
    String thumbnailUrl;
    Double averageRating;
    String typeName;
    String restaurantTierName;
}
