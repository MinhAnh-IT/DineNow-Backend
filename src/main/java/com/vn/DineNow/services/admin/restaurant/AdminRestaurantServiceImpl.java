package com.vn.DineNow.services.admin.restaurant;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.enums.RestaurantStatus;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.RestaurantMapper;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseForAdmin;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.services.owner.restaurant.restaurantImages.RestaurantImageService;
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
    RestaurantImageService restaurantImageService;

    /**
     * Updates the status of a restaurant.
     *
     * @param restaurantID the ID of the restaurant to update
     * @param status       the new status to set
     * @return true if the operation was successful; false otherwise
     * @throws CustomException if the restaurant is not found or if the status transition is not allowed
     */
    @Override
    public boolean updateRestaurantStatus(long restaurantID, RestaurantStatus status) throws CustomException {

        Restaurant restaurant = restaurantRepository.findById(restaurantID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantID)));
        if (isTransitionAllowed(restaurant.getStatus(), status)) {
            restaurant.setStatus(status);
            restaurantRepository.save(restaurant);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a paginated list of all restaurants by their status.
     *
     * @param status the status of the restaurants to retrieve
     * @param page   the page number to retrieve (zero-based)
     * @param size   the number of records per page
     * @return a list of simplified restaurant response DTOs
     * @throws CustomException if an error occurs while retrieving the data
     */
    @Override
    public List<RestaurantSimpleResponseForAdmin> getAllRestaurantsByStatus(RestaurantStatus status, int page, int size) throws CustomException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Restaurant> restaurants = restaurantRepository.findAllByStatus(status, pageable);
        return restaurants.getContent().stream()
                .map(restaurantMapper::toSimpleDTOForAdmin)
                .toList();
    }

    /**
     * Retrieves detailed information about a specific restaurant.
     *
     * @param restaurantID the ID of the restaurant to retrieve
     * @return a detailed restaurant response DTO
     * @throws CustomException if the restaurant is not found
     */
    @Override
    public RestaurantResponseDTO getRestaurantDetails(long restaurantID) throws CustomException {
        Restaurant restaurant = restaurantRepository.findById(restaurantID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantID)));
        var response = restaurantMapper.toDTO(restaurant);
        response.setImageUrls(restaurantImageService.getImageUrlsByRestaurantId(restaurantID));
        return response;
    }

    /**
     * Checks if a transition between two restaurant statuses is allowed.
     *
     * @param current the current status of the restaurant
     * @param target  the target status to transition to
     * @return true if the transition is allowed; false otherwise
     */
    private boolean isTransitionAllowed(RestaurantStatus current, RestaurantStatus target) {
        return switch (current) {
            case PENDING -> target == RestaurantStatus.APPROVED || target == RestaurantStatus.REJECTED;
            case APPROVED -> target == RestaurantStatus.BLOCKED;
            case BLOCKED -> target == RestaurantStatus.APPROVED;
            default -> false;
        };
    }
}
