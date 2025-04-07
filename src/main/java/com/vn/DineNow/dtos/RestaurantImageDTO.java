package com.vn.DineNow.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class RestaurantImageDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String imageUrl;

    private OffsetDateTime uploadedAt;

    @NotNull
    private Long restaurant;

}
