package com.vn.DineNow.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class ReservationDTO {

    private Long id;

    @NotNull
    private OffsetDateTime reservationTime;

    @NotNull
    private Integer numberOfPeople;

    @Size(max = 255)
    private String status;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    @NotNull
    private Long customer;

    @NotNull
    private Long restaurant;

    @NotNull
    private Integer numberOfChild;
}
