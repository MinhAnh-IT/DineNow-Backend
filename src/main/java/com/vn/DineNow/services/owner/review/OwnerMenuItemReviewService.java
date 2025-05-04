package com.vn.DineNow.services.owner.review;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.review.ReviewResponse;

import java.util.List;

public interface OwnerMenuItemReviewService {
    List<ReviewResponse> getAllReviewsByMenuItemId(long ownerId, long menuItemId, int page, int size) throws CustomException;
}
