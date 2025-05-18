package com.vn.DineNow.services.admin.restaurantTier;

import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.RestaurantTiersMapper;
import com.vn.DineNow.payload.request.restaurantTiers.RestaurantTieUpdateRequest;
import com.vn.DineNow.payload.request.restaurantTiers.RestaurantTiersRequest;
import com.vn.DineNow.payload.response.restaurantTiers.RestaurantTiersResponse;
import com.vn.DineNow.repositories.RestaurantTierRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantTierServiceImpl implements RestaurantTierService{
    RestaurantTierRepository restaurantTierRepository;
    RestaurantTiersMapper restaurantTiersMapper;
    @Override
    public RestaurantTiersResponse createRestaurantTier(RestaurantTiersRequest request) {
        var restaurantTier = restaurantTiersMapper.toEntity(request);
        return restaurantTiersMapper.toDTO(restaurantTierRepository.save(restaurantTier));
    }

    @Override
    public RestaurantTiersResponse updateRestaurantTier(Long id, RestaurantTieUpdateRequest request) throws CustomException {
        var restaurantTier = restaurantTierRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "restaurant tier", String.valueOf(id)));
        restaurantTiersMapper.updateEntity(request, restaurantTier);
        restaurantTierRepository.save(restaurantTier);
        return restaurantTiersMapper.toDTO(restaurantTier);
    }

    @Override
    public List<RestaurantTiersResponse> getAllRestaurantTiers() {
        var restaurantTiers = restaurantTierRepository.findAll();
        return restaurantTiers.stream()
                .map(restaurantTiersMapper::toDTO)
                .toList();
    }
}
