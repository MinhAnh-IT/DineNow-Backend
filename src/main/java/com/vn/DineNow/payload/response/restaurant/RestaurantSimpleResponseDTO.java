package com.vn.DineNow.payload.response.restaurant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantSimpleResponseDTO {
    private Long id;
    private String name;
    private String address;
    private Double averageRating;
    private String restaurantTierName;
    private String thumbnailUrl;
    private String typeName;
}
