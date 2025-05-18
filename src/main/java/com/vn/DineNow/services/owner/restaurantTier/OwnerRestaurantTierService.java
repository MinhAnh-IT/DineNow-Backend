package com.vn.DineNow.services.owner.restaurantTier;

import com.vn.DineNow.payload.response.restaurantTiers.RestaurantTierForOwner;

import java.util.List;

public interface OwnerRestaurantTierService{
    List<RestaurantTierForOwner> getAllRestaurantTiers();
}
