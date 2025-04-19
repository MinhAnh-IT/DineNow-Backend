package com.vn.DineNow.services.favoriteRestaurant;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.response.restaurant.FavoriteRestaurantResponseDTO;

import java.util.List;

public interface IFavoriteRestaurantService {
    List<FavoriteRestaurantResponseDTO> getListFavoriteRestaurantOfUser(long userID) throws CustomException;
    FavoriteRestaurantResponseDTO addFavoriteRestaurantForUser(long userID, long restaurantID) throws CustomException;

    boolean removeFavorite(long id, long restaurantID) throws CustomException;
}
