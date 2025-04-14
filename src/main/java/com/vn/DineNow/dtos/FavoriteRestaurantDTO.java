package com.vn.DineNow.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
@Builder
public class FavoriteRestaurantDTO {

    private Long id;

    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @NotNull
    private Long user;

    @NotNull
    private Long restaurant;

}
