package com.vn.DineNow.services.owner.restaurant;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.restaurant.RestaurantRequestDTO;
import com.vn.DineNow.payload.request.restaurant.RestaurantUpdateDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;

import java.util.List;

public interface OwnerRestaurantService {
    RestaurantResponseDTO createRestaurant(long userId, RestaurantRequestDTO requestDTO)
            throws CustomException;
    RestaurantResponseDTO updateRestaurant(long owner, long restaurantId, RestaurantUpdateDTO restaurantUpdateDTO) throws CustomException;
    List<RestaurantSimpleResponseDTO> getRestaurantByOwner(long ownerId) throws CustomException;
}
