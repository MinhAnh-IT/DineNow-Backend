package com.vn.DineNow.services.admin.restaurant;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.enums.RestaurantStatus;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.RestaurantMapper;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseForAdmin;
import com.vn.DineNow.repositories.RestaurantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing restaurant data from the admin side.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminRestaurantServiceImpl implements AdminRestaurantService {
    RestaurantRepository restaurantRepository;
    RestaurantMapper restaurantMapper;

    /**
     * Approves a restaurant by changing its status to APPROVED.
     *
     * @param restaurantID the ID of the restaurant to approve
     * @return true if the operation was successful
     * @throws CustomException if the restaurant is not found or cannot be approved
     */
    @Override
    public boolean approveRestaurant(long restaurantID) throws CustomException {
        Restaurant restaurant = getPendingRestaurantOrThrow(restaurantID);
        restaurant.setStatus(RestaurantStatus.APPROVED);
        restaurantRepository.save(restaurant);
        return true;
    }

    /**
     * Rejects a restaurant by changing its status to REJECTED.
     *
     * @param restaurantID the ID of the restaurant to reject
     * @return true if the operation was successful
     * @throws CustomException if the restaurant is not found or cannot be rejected
     */
    @Override
    public boolean rejectRestaurant(long restaurantID) throws CustomException {
        Restaurant restaurant = getPendingRestaurantOrThrow(restaurantID);
        restaurant.setStatus(RestaurantStatus.REJECTED);
        restaurantRepository.save(restaurant);
        return true;
    }

    /**
     * Blocks an approved restaurant by changing its status to BLOCKED.
     *
     * @param restaurantID the ID of the restaurant to block
     * @return true if the restaurant was successfully blocked; false otherwise
     * @throws CustomException if the restaurant is not found
     */
    @Override
    public boolean blockRestaurant(long restaurantID) throws CustomException {
        Restaurant restaurant = restaurantRepository.findById(restaurantID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantID)));

        if (restaurant.getStatus() == RestaurantStatus.APPROVED) {
            restaurant.setStatus(RestaurantStatus.BLOCKED);
            restaurantRepository.save(restaurant);
            return true;
        }
        return false;
    }

    /**
     * Unblocks a blocked restaurant by changing its status back to APPROVED.
     *
     * @param restaurantID the ID of the restaurant to unblock
     * @return true if the restaurant was successfully unblocked; false otherwise
     * @throws CustomException if the restaurant is not found
     */
    @Override
    public boolean unblockRestaurant(long restaurantID) throws CustomException {
        Restaurant restaurant = restaurantRepository.findById(restaurantID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantID)));

        if (restaurant.getStatus() != RestaurantStatus.BLOCKED){
            throw new CustomException(StatusCode.INVALID_ACTION, "Only BLOCKED restaurant can be unblocked");
        }

        restaurant.setStatus(RestaurantStatus.APPROVED);
        restaurantRepository.save(restaurant);
        return true;
    }

    /**
     * Retrieves a paginated list of all pending restaurants.
     *
     * @param page the page number to retrieve (zero-based)
     * @param size the number of records per page
     * @return a list of simplified restaurant response DTOs
     * @throws CustomException if an error occurs while retrieving the data
     */
    @Override
    public List<RestaurantSimpleResponseForAdmin> getAllPendingRestaurants(int page, int size) throws CustomException {
        Pageable pageable = PageRequest.of(page, size);
        var restaurants = restaurantRepository.findAllByStatus(RestaurantStatus.PENDING, pageable);
        return restaurants.getContent().stream()
                .map(restaurantMapper::toSimpleDTOForAdmin)
                .toList();
    }

    /**
     * Retrieves a paginated list of all approved restaurants.
     *
     * @param page the page number to retrieve (zero-based)
     * @param size the number of records per page
     * @return a list of simplified restaurant response DTOs
     * @throws CustomException if an error occurs while retrieving the data
     */
    @Override
    public List<RestaurantSimpleResponseForAdmin> getAllApprovedRestaurants(int page, int size) throws CustomException {
        Pageable pageable = PageRequest.of(page, size);
        var restaurants = restaurantRepository.findAllByStatus(RestaurantStatus.APPROVED, pageable);
        return restaurants.getContent().stream()
                .map(restaurantMapper::toSimpleDTOForAdmin)
                .toList();
    }

    /**
     * Retrieves a paginated list of all blocked restaurants.
     *
     * @param page the page number to retrieve (zero-based)
     * @param size the number of records per page
     * @return a list of simplified restaurant response DTOs
     * @throws CustomException if an error occurs while retrieving the data
     */
    @Override
    public List<RestaurantSimpleResponseForAdmin> getAllBlockedRestaurants(int page, int size) throws CustomException {
        Pageable pageable = PageRequest.of(page, size);
        var restaurants = restaurantRepository.findAllByStatus(RestaurantStatus.BLOCKED, pageable);
        return restaurants.getContent().stream()
                .map(restaurantMapper::toSimpleDTOForAdmin)
                .toList();
    }

    /**
     * Retrieves a paginated list of all rejected restaurants.
     *
     * @param page the page number to retrieve (zero-based)
     * @param size the number of records per page
     * @return a list of simplified restaurant response DTOs
     * @throws CustomException if an error occurs while retrieving the data
     */
    @Override
    public List<RestaurantSimpleResponseForAdmin> getAllRejectedRestaurants(int page, int size) throws CustomException {
        Pageable pageable = PageRequest.of(page, size);
        var restaurants = restaurantRepository.findAllByStatus(RestaurantStatus.REJECTED, pageable);
        return restaurants.getContent().stream()
                .map(restaurantMapper::toSimpleDTOForAdmin)
                .toList();
    }


    private Restaurant getPendingRestaurantOrThrow(long restaurantID) throws CustomException {
        Restaurant restaurant = restaurantRepository.findById(restaurantID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantID)));

        if (!restaurant.getStatus().equals(RestaurantStatus.PENDING)) {
            throw new CustomException(StatusCode.INVALID_ACTION, "Only PENDING restaurant can be updated");
        }
        return restaurant;
    }
}
