package com.vn.DineNow.services.customer.review;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.review.ReviewRequestDTO;
import com.vn.DineNow.payload.response.review.ReviewResponse;

import java.util.List;

public interface MenuItemReviewService {
    ReviewResponse addReview(long customerId, long menuItemId, ReviewRequestDTO reviewDto) throws CustomException;
    List<ReviewResponse> getAllReviewsByMenuItemId(long menuItemId, int page, int size) throws CustomException;
}
