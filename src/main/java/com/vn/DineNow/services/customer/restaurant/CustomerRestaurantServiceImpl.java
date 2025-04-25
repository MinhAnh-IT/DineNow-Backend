package com.vn.DineNow.services.customer.restaurant;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.enums.RestaurantStatus;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.RestaurantMapper;
import com.vn.DineNow.payload.request.restaurant.SearchRestaurantDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.services.common.cache.RedisService;
import com.vn.DineNow.services.owner.restaurant.restaurantImages.RestaurantImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerRestaurantServiceImpl implements CustomerRestaurantService {
    final RestaurantRepository restaurantRepository;
    final RestaurantImageService restaurantImageService;
    final RestaurantMapper restaurantMapper;
    final RedisService redisService;

    @Value("${DineNow.key.cache-restaurant}")
    String keyRedis;

    @Override
    public Page<RestaurantSimpleResponseDTO> getAllRestaurantStatusApproved(int page, int size) {
        Pageable pageable = (Pageable) PageRequest.of(page, size);
        var restaurants = restaurantRepository.findAllByStatus(RestaurantStatus.APPROVED, pageable);
        return restaurants.map(restaurantMapper::toSimpleDTO);
    }

    @Override
    public RestaurantResponseDTO getRestaurantByID(long restaurantId) throws CustomException{
        String key = keyRedis + String.valueOf(restaurantId);
        RestaurantResponseDTO cachedDTO = redisService.getObject(key, RestaurantResponseDTO.class);
        if (cachedDTO != null)
            return cachedDTO;
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId)));

        RestaurantResponseDTO restaurantResponseDTO = restaurantMapper.toDTO(restaurant);
        restaurantResponseDTO.setImageUrls(restaurantImageService.getImageUrlsByRestaurantId(restaurantId));

        redisService.saveObject(key, restaurantResponseDTO, 20, TimeUnit.MINUTES);
        return restaurantResponseDTO;
    }

    @Override
    public Page<RestaurantSimpleResponseDTO> searchRestaurant(SearchRestaurantDTO searchRestaurantDTO, int page, int size)
            throws CustomException {
        String province = searchRestaurantDTO.getProvince();
        String restaurantName = searchRestaurantDTO.getRestaurantName();
        Pageable pageable = PageRequest.of(page, size);
        if (province == null || province.isBlank()) province = "";
        if (restaurantName == null || restaurantName.isBlank()) restaurantName = "";

        var restaurants = restaurantRepository.searchRestaurantByCityAndName(province.trim(), restaurantName.trim(), pageable);

        return restaurants.map(restaurantMapper::toSimpleDTO);
    }
}
