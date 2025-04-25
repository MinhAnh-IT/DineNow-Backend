package com.vn.DineNow.payload.request.mainCategory;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainCategoryRequest {
    @NotBlank(message = "Main category name must not be blank")
    String name;
    String description;
}
