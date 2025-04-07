package com.vn.DineNow.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class FavoriteRestaurantDTO {

    private Long id;

    private OffsetDateTime createdAt;

    @NotNull
    private Long user;

    @NotNull
    private Long restaurant;

}
