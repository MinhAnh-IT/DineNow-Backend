package com.vn.DineNow.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
public class MenuItemReviewDTO {

    private Long id;

    private Long userId;

    private String username;

    private Integer rating;

    private String comment;

    private OffsetDateTime createdAt;
}
