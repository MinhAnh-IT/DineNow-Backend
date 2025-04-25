package com.vn.DineNow.payload.response.foodCategory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodCategoryResponseDTO {

    Long id;

    String name;

    String description;

    Long restaurantId;

    String restaurantName;

    Long mainCategoryId;

    String mainCategoryName;
}
