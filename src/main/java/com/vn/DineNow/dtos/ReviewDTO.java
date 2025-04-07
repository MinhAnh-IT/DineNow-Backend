package com.vn.DineNow.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;


@Getter
@Setter
public class ReviewDTO {

    private Long id;

    private Integer rating;

    private String comment;

    private OffsetDateTime createdAt;

    @NotNull
    private Long customer;

    @NotNull
    private Long restaurant;

}
