package com.vn.DineNow.services.customer.review;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.review.ReviewRequestDTO;
import com.vn.DineNow.payload.response.review.ReviewResponse;

import java.util.List;

public interface RestaurantReviewService {
    ReviewResponse addReview(long customerId, long restaurantId, ReviewRequestDTO reviewDto) throws CustomException;

    List<ReviewResponse> getAllReviewsByRestaurantId(long restaurantId, int page, int size) throws CustomException;
}
