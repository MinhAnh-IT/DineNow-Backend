package com.vn.DineNow.payload.response.restaurant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vn.DineNow.payload.response.restaurantType.RestaurantTypeSimple;
import com.vn.DineNow.enums.RestaurantStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class RestaurantResponseDTO {
    Long id;
    String name;
    String address;
    String phone;
    String restaurantTierName;
    String description;
    RestaurantTypeSimple type;
    double averageRating;
    RestaurantStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime updatedAt;

    List<String> imageUrls;
}
