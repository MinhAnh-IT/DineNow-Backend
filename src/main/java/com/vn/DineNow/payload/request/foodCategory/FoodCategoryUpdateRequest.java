package com.vn.DineNow.payload.request.foodCategory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodCategoryUpdateRequest {
    String name;

    String description;

    Long mainCategoryId;
}
