package com.vn.DineNow.services.admin.restaurantTier;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.restaurantTiers.RestaurantTieUpdateRequest;
import com.vn.DineNow.payload.request.restaurantTiers.RestaurantTiersRequest;
import com.vn.DineNow.payload.response.restaurantTiers.RestaurantTiersResponse;

import java.util.List;

public interface RestaurantTierService {
    RestaurantTiersResponse createRestaurantTier(RestaurantTiersRequest request);
    RestaurantTiersResponse updateRestaurantTier(Long id, RestaurantTieUpdateRequest request) throws CustomException;
    List<RestaurantTiersResponse> getAllRestaurantTiers();
}
