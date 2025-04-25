package com.vn.DineNow.payload.response.restaurant;

import com.vn.DineNow.dtos.RestaurantTypeDTO;
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
    private RestaurantTypeDTO type;
    private String restaurantTierName;
}
