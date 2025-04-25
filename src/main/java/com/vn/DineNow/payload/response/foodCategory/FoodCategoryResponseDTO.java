package com.vn.DineNow.payload.response.foodCategory;

import com.vn.DineNow.entities.MainCategory;
import com.vn.DineNow.payload.response.mainCategory.MainCategoryResponse;
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

    String restaurantName;

    String mainCategoryName;
}
