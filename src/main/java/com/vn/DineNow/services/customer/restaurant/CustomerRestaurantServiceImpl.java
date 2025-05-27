package com.vn.DineNow.services.customer.restaurant;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.enums.RestaurantStatus;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.RestaurantMapper;
import com.vn.DineNow.payload.request.googleMaps.LocationRequest;
import com.vn.DineNow.payload.request.restaurant.SearchRestaurantDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.services.common.cache.RedisService;
import com.vn.DineNow.services.customer.reservation.CustomerReservationService;
import com.vn.DineNow.services.owner.restaurant.restaurantImages.RestaurantImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
    final CustomerReservationService reservationService;

    @Value("${DineNow.key.cache-restaurant}")
    String keyRedis;

    private RestaurantSimpleResponseDTO toDTOWithReservationCount(Restaurant restaurant) {
        var dto = restaurantMapper.toSimpleDTO(restaurant);
        try {
            dto.setReservationCount(reservationService.getTotalReservationByRestaurantId(dto.getId()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dto;
    }

    /**
     * Retrieves paginated list of all restaurants with APPROVED status.
     */
    @Override
    public List<RestaurantSimpleResponseDTO> getAllRestaurantStatusApproved(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var restaurants = restaurantRepository.findAllByStatus(RestaurantStatus.APPROVED, pageable);
        return restaurants.stream()
                .map(this::toDTOWithReservationCount)
                .toList();
    }

    /**
     * Retrieves full detail of a restaurant by ID, with caching.
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

        try {
            restaurantResponseDTO.setReservationCount(reservationService.getTotalReservationByRestaurantId(restaurantId));
        } catch (Exception e) {
            throw new CustomException(StatusCode.BAD_REQUEST);
        }

        // Cache result for 20 minutes
        redisService.saveObject(key, restaurantResponseDTO, 20, TimeUnit.MINUTES);
        return restaurantResponseDTO;
    }

    /**
     * Searches for restaurants by name and province (case-insensitive, partial matches).
     */
    @Override
    public List<RestaurantSimpleResponseDTO> searchRestaurant(SearchRestaurantDTO searchRestaurantDTO, int page, int size)
            throws CustomException {
        String province = searchRestaurantDTO.getProvince();
        String restaurantName = searchRestaurantDTO.getRestaurantName();

        Pageable pageable = PageRequest.of(page, size);

        if (province == null || province.isBlank()) province = "";
        if (restaurantName == null || restaurantName.isBlank()) restaurantName = "";

        var restaurants = restaurantRepository.searchRestaurantByCityAndName(
                province.trim(), restaurantName.trim(), pageable);

        // restaurants là Page<Restaurant>
        return restaurants.stream()
                .map(this::toDTOWithReservationCount)
                .toList();
    }

    /**
     * Retrieves all restaurants by type ID.
     */
    @Override
    public List<RestaurantSimpleResponseDTO> getAllRestaurantByTypeId(long typeId, int page, int size) throws CustomException {
        Pageable pageable = PageRequest.of(page, size);
        var restaurants = restaurantRepository.findAllByType_Id(typeId, pageable);
        return restaurants.stream()
                .map(this::toDTOWithReservationCount)
                .toList();
    }

    /**
     * Retrieves a list of featured restaurants.
     */
    @Override
    public List<RestaurantSimpleResponseDTO> GetListOfFeaturedRestaurants() {
        var restaurants = restaurantRepository.findTopFeaturedRestaurants();
        return restaurants.stream()
                .map(this::toDTOWithReservationCount)
                .toList();
    }

    /**
     * Finds restaurants within a specified radius from a given location.
     */
    @Override
    public List<RestaurantSimpleResponseDTO> findRestaurantsWithinRadius(
            LocationRequest request, double radius, int page, int size) throws CustomException {

        List<Restaurant> restaurants = restaurantRepository.findRestaurantsWithinRadius(
                request.getLat(), request.getLng(), radius);

        int start = Math.max(0, page * size);
        int end = Math.min(start + size, restaurants.size());
        if (start >= end) return List.of();

        return restaurants.subList(start, end)
                .stream()
                .map(this::toDTOWithReservationCount)
                .toList();
    }
}
