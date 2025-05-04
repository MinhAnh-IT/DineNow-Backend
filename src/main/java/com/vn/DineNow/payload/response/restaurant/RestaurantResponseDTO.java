package com.vn.DineNow.payload.response.restaurant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vn.DineNow.payload.response.restaurantType.RestaurantTypeSimple;
import com.vn.DineNow.enums.RestaurantStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponseDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String restaurantTierName;
    private String description;
    private RestaurantTypeSimple type;
    private double averageRating;
    private RestaurantStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime updatedAt;

    private List<String> imageUrls;
}
