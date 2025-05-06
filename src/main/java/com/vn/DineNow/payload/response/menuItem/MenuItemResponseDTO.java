package com.vn.DineNow.payload.response.menuItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vn.DineNow.entities.FoodCategory;
import com.vn.DineNow.payload.response.foodCategory.FoodCategoryResponseDTO;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
public class MenuItemResponseDTO {
    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    private String description;
    private double averageRating;
    @NotNull
    @Digits(integer = 12, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    private Boolean available;

    private String imageUrl;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private FoodCategoryResponseDTO category;
}
