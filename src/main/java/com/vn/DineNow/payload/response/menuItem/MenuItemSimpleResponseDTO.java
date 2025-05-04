package com.vn.DineNow.payload.response.menuItem;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MenuItemSimpleResponseDTO {
    private Long id;
    private String name;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;
    private double averageRating;
    private String imageUrl;
    private String typeName;
}

