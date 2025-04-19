package com.vn.DineNow.services.restaurant;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.restaurant.RestaurantRequestDTO;
import com.vn.DineNow.payload.request.restaurant.RestaurantUpdateDTO;
import com.vn.DineNow.payload.request.restaurant.SearchRestaurantDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import org.springframework.data.domain.Page;



public interface IRestaurantService {
    RestaurantResponseDTO createRestaurant(long userId, RestaurantRequestDTO requestDTO)
            throws CustomException;
    Page<RestaurantSimpleResponseDTO> getAllRestaurant(int page, int size);
    RestaurantResponseDTO getRestaurantByID(long restaurantId) throws CustomException;
    Page<RestaurantSimpleResponseDTO> searchRestaurant(SearchRestaurantDTO searchRestaurantDTO, int page, int size)
            throws CustomException;
    RestaurantResponseDTO updateRestaurant(long owner, long restaurantId, RestaurantUpdateDTO restaurantUpdateDTO) throws CustomException;
}
