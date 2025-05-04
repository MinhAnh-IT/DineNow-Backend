package com.vn.DineNow.services.admin.restaurant;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseForAdmin;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminRestaurantService {
    boolean approveRestaurant(long restaurantID) throws CustomException;
    boolean rejectRestaurant(long restaurantID) throws CustomException;
    boolean blockRestaurant(long restaurantID) throws CustomException;
    boolean unblockRestaurant(long restaurantID) throws CustomException;
    List<RestaurantSimpleResponseForAdmin> getAllPendingRestaurants(int page, int size) throws CustomException;
    List<RestaurantSimpleResponseForAdmin> getAllApprovedRestaurants(int page, int size) throws CustomException;
    List<RestaurantSimpleResponseForAdmin> getAllBlockedRestaurants(int page, int size) throws CustomException;
    List<RestaurantSimpleResponseForAdmin> getAllRejectedRestaurants(int page, int size) throws CustomException;

}
