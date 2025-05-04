package com.vn.DineNow.payload.response.review;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class ReviewResponse {
    String reviewerName;
    String comment;
    OffsetDateTime reviewDate;
    int rating;
}
