package com.vn.DineNow.payload.request.mainCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainCategoryRequest {
    @NotNull(message = "Main category name must not be null")
    @NotBlank(message = "Main category name must not be blank")
    private String name;

    @NotNull(message = "Main category description must not be null")
    @NotBlank(message = "Main category description must not be blank")
    String description;
}
