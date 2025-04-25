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

/**
 * Service implementation for retrieving restaurant data for customer users.
 * Includes caching and search capabilities.
 */
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

    /**
     * Retrieves paginated list of all restaurants with APPROVED status.
     *
     * @param page the page number (0-based)
     * @param size the number of records per page
     * @return paginated list of simplified restaurant DTOs
     */
    @Override
    public Page<RestaurantSimpleResponseDTO> getAllRestaurantStatusApproved(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var restaurants = restaurantRepository.findAllByStatus(RestaurantStatus.APPROVED, pageable);
        return restaurants.map(restaurantMapper::toSimpleDTO);
    }

    /**
     * Retrieves full detail of a restaurant by ID, with caching.
     *
     * @param restaurantId the ID of the restaurant
     * @return the full restaurant response DTO
     * @throws CustomException if restaurant is not found
     */
    @Override
    public RestaurantResponseDTO getRestaurantByID(long restaurantId) throws CustomException {
        String key = keyRedis + restaurantId;

        // Check Redis cache first
        RestaurantResponseDTO cachedDTO = redisService.getObject(key, RestaurantResponseDTO.class);
        if (cachedDTO != null) return cachedDTO;

        // Fallback to DB
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId)));

        RestaurantResponseDTO restaurantResponseDTO = restaurantMapper.toDTO(restaurant);

        // Attach image URLs (from image service)
        restaurantResponseDTO.setImageUrls(restaurantImageService.getImageUrlsByRestaurantId(restaurantId));

        // Cache result for 20 minutes
        redisService.saveObject(key, restaurantResponseDTO, 20, TimeUnit.MINUTES);

        return restaurantResponseDTO;
    }

    /**
     * Searches for restaurants by name and province (case-insensitive, partial matches).
     *
     * @param searchRestaurantDTO the search criteria object
     * @param page the page number
     * @param size the number of results per page
     * @return paginated list of matched restaurants
     * @throws CustomException if a search failure occurs
     */
    @Override
    public Page<RestaurantSimpleResponseDTO> searchRestaurant(SearchRestaurantDTO searchRestaurantDTO, int page, int size)
            throws CustomException {
        String province = searchRestaurantDTO.getProvince();
        String restaurantName = searchRestaurantDTO.getRestaurantName();

        Pageable pageable = PageRequest.of(page, size);

        // Normalize null/blank search inputs to empty string
        if (province == null || province.isBlank()) province = "";
        if (restaurantName == null || restaurantName.isBlank()) restaurantName = "";

        var restaurants = restaurantRepository.searchRestaurantByCityAndName(
                province.trim(), restaurantName.trim(), pageable);

        return restaurants.map(restaurantMapper::toSimpleDTO);
    }
}
