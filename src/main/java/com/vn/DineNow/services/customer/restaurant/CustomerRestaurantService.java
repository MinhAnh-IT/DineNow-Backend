package com.vn.DineNow.services.customer.restaurant;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.restaurant.SearchRestaurantDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import org.springframework.data.domain.Page;


public interface CustomerRestaurantService {
    Page<RestaurantSimpleResponseDTO> getAllRestaurantStatusApproved(int page, int size);
    RestaurantResponseDTO getRestaurantByID(long restaurantId) throws CustomException;
    Page<RestaurantSimpleResponseDTO> searchRestaurant(SearchRestaurantDTO searchRestaurantDTO, int page, int size)
            throws CustomException;
}

