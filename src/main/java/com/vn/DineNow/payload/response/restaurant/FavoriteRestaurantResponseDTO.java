package com.vn.DineNow.payload.response.restaurant;

import lombok.*;

@Getter
@Setter
@Builder
public class FavoriteRestaurantResponseDTO {
    private Long id;
    private String name;
    private String address;
    private String thumbnailUrl;
    private Double averageRating;
    private String typeName;
    private String restaurantTierName;
}
