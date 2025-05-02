package com.vn.DineNow.services.admin.restaurantType;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.restaurantType.RestaurantTypeRequest;
import com.vn.DineNow.payload.request.restaurantType.RestaurantTypeUpdateRequest;
import com.vn.DineNow.payload.response.restaurantType.RestaurantTypeResponse;

import java.util.List;

public interface AdminRestaurantTypeService {
    RestaurantTypeResponse createRestaurantType(RestaurantTypeRequest request) throws CustomException;
    RestaurantTypeResponse updateRestaurantType(long id, RestaurantTypeUpdateRequest request) throws CustomException;
    List<RestaurantTypeResponse> getAllRestaurantType()  throws CustomException;
    boolean deleteRestaurantType(long id) throws CustomException;
}
