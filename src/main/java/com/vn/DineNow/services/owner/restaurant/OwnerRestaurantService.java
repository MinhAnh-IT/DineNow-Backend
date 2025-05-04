package com.vn.DineNow.services.owner.restaurant;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.enums.RestaurantStatus;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.restaurant.RestaurantRequestDTO;
import com.vn.DineNow.payload.request.restaurant.RestaurantUpdateDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;

import java.util.List;

public interface OwnerRestaurantService {
    RestaurantResponseDTO createRestaurant(long ownerId, RestaurantRequestDTO requestDTO)
            throws CustomException;
    RestaurantResponseDTO updateRestaurant(long ownerId, long restaurantId, RestaurantUpdateDTO restaurantUpdateDTO) throws CustomException;
    List<RestaurantSimpleResponseDTO> getRestaurantByOwner(long ownerId) throws CustomException;
    boolean updateRestaurantStatus(long ownerId, long restaurantId, RestaurantStatus status) throws CustomException;
    void updateAvgRatingForRestaurant(Restaurant restaurant) throws CustomException;
}
