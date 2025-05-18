package com.vn.DineNow.services.owner.restaurantTier;

import com.vn.DineNow.mapper.RestaurantTiersMapper;
import com.vn.DineNow.payload.response.restaurantTiers.RestaurantTierForOwner;
import com.vn.DineNow.repositories.RestaurantTierRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OwnerRestaurantTierServiceImpl implements OwnerRestaurantTierService{
    RestaurantTierRepository restaurantTierRepository;
    RestaurantTiersMapper restaurantTiersMapper;

    @Override
    public List<RestaurantTierForOwner> getAllRestaurantTiers() {
        return restaurantTierRepository.findAll()
                .stream()
                .map(restaurantTiersMapper::toOwnerDTO)
                .toList();
    }
}
