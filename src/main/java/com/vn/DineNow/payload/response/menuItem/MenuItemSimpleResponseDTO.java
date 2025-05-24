package com.vn.DineNow.payload.response.menuItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuItemSimpleResponseDTO {
    Long id;
    String name;
    String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    BigDecimal price;
    double averageRating;
    String imageUrl;
    String typeName;
    long restaurantId;
}

