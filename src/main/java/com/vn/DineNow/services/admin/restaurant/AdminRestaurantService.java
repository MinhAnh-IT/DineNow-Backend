package com.vn.DineNow.services.admin.restaurant;

import com.vn.DineNow.enums.RestaurantStatus;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseForAdmin;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminRestaurantService {
    boolean updateRestaurantStatus(long restaurantID, RestaurantStatus status) throws CustomException;
    List<RestaurantSimpleResponseForAdmin> getAllRestaurantsByStatus(RestaurantStatus status, int page, int size) throws CustomException;
    RestaurantResponseDTO getRestaurantDetails(long restaurantID) throws CustomException;
}
