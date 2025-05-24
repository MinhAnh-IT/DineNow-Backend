package com.vn.DineNow.payload.response.menuItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vn.DineNow.entities.FoodCategory;
import com.vn.DineNow.payload.response.foodCategory.FoodCategoryResponseDTO;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuItemResponseDTO {
    Long id;

    @NotNull
    @Size(max = 255)
    String name;

    String description;
    double averageRating;
    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal price;

    Boolean available;

    String imageUrl;

    OffsetDateTime createdAt;

    OffsetDateTime updatedAt;

    FoodCategoryResponseDTO category;
}
