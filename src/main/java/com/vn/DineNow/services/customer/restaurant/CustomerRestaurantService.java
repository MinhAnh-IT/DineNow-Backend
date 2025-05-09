package com.vn.DineNow.services.customer.restaurant;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.restaurant.SearchRestaurantDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseForAdmin;
import org.springframework.data.domain.Page;

import java.util.List;


public interface CustomerRestaurantService {
    List<RestaurantSimpleResponseDTO> getAllRestaurantStatusApproved(int page, int size);
    RestaurantResponseDTO getRestaurantByID(long restaurantId) throws CustomException;
    List<RestaurantSimpleResponseDTO> searchRestaurant(SearchRestaurantDTO searchRestaurantDTO, int page, int size)
            throws CustomException;
    List<RestaurantSimpleResponseDTO> getAllRestaurantByTypeId(long typeId, int page, int size)
            throws CustomException;
    List<RestaurantSimpleResponseDTO>GetListOfFeaturedRestaurants();

}

