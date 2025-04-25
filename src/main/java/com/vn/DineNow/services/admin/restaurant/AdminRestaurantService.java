package com.vn.DineNow.services.admin.restaurant;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import org.springframework.data.domain.Page;

public interface AdminRestaurantService {
    Page<RestaurantSimpleResponseDTO> getAllRestaurants(int page, int size) throws CustomException;
}
