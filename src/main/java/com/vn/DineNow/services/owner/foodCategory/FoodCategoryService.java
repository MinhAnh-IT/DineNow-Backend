package com.vn.DineNow.services.owner.foodCategory;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.foodCategory.FoodCategoryRequest;
import com.vn.DineNow.payload.request.foodCategory.FoodCategoryUpdateRequest;
import com.vn.DineNow.payload.response.foodCategory.FoodCategoryResponseDTO;

import java.util.List;

public interface FoodCategoryService {
    FoodCategoryResponseDTO addNewFoodCategory(long ownerId,long restaurantId, FoodCategoryRequest request)
            throws CustomException;
    FoodCategoryResponseDTO updateFoodCategory(long ownerId, long foodCategoryId, FoodCategoryUpdateRequest request)
            throws CustomException;
    boolean deleteFoodCategory(long ownerId, long foodCategoryId) throws CustomException;
    List<FoodCategoryResponseDTO> getAllFoodCategoryForRestaurant(long ownerId, long restaurantId)
            throws CustomException;


}
