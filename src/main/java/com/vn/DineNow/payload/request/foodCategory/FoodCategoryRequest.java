package com.vn.DineNow.payload.request.foodCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodCategoryRequest {

    @NotBlank(message = "Food category name must not be blank")
    String name;

    @NotBlank(message = "Food category description must not be blank")
    String description;

    @NotNull(message = "Restaurant ID is required")
    long restaurantId;

    @NotNull(message = "Main category ID is required")
    long mainCategoryId;
}
