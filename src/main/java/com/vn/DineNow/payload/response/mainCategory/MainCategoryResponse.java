package com.vn.DineNow.payload.response.mainCategory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainCategoryResponse {
    String name;
    String description;
}
