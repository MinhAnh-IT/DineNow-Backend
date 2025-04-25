package com.vn.DineNow.services.admin.restaurant;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.RestaurantMapper;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.repositories.RestaurantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminRestaurantServiceImpl implements AdminRestaurantService {
    RestaurantRepository restaurantRepository;
    RestaurantMapper restaurantMapper;

    @Override
    public Page<RestaurantSimpleResponseDTO> getAllRestaurants(int page, int size) throws CustomException {
        Pageable pageable = PageRequest.of(page, size);
        var restaurants = restaurantRepository.findAll(pageable);

        return restaurants.map(restaurantMapper::toSimpleDTO);
    }



}
