package com.vn.DineNow.payload.request.favoriteRestaurant;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
@Builder
public class FavoriteRestaurantRequest {

    private Long id;

    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @NotNull
    private Long user;

    @NotNull
    private Long restaurant;

}
