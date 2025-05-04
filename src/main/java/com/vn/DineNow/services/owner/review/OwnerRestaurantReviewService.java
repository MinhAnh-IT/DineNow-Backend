package com.vn.DineNow.services.owner.review;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.review.ReviewResponse;

import java.util.List;

public interface OwnerRestaurantReviewService {
    List<ReviewResponse> getAllReviewsByRestaurantId(long ownerId,long restaurantId, int page, int size) throws CustomException;
}
