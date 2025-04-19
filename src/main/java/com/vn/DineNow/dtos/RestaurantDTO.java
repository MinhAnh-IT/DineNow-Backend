package com.vn.DineNow.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vn.DineNow.entities.RestaurantType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Getter
@Setter
public class RestaurantDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private String address;

    @NotNull
    @Size(max = 20)
    private String phone;

    @NotNull
    private Boolean enabled = true;

    private String description;

    @Digits(integer = 5, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal averageRating;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private RestaurantType type;

    @NotNull
    private Long owner;

}
