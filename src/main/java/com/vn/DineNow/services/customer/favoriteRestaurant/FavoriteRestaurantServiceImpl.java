package com.vn.DineNow.services.customer.favoriteRestaurant;

import com.vn.DineNow.dtos.FavoriteRestaurantDTO;
import com.vn.DineNow.entities.FavoriteRestaurant;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.RestaurantStatus;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.FavoriteRestaurantMapper;
import com.vn.DineNow.mapper.RestaurantMapper;
import com.vn.DineNow.payload.response.restaurant.FavoriteRestaurantResponseDTO;
import com.vn.DineNow.repositories.FavoriteRestaurantRepository;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing a customer's favorite restaurants.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteRestaurantServiceImpl implements FavoriteRestaurantService {

    FavoriteRestaurantRepository favoriteRestaurantRepository;
    UserRepository userRepository;
    RestaurantMapper restaurantMapper;
    FavoriteRestaurantMapper favoriteRestaurantMapper;
    RestaurantRepository restaurantRepository;

    /**
     * Gets the list of favorite restaurants for a specific user.
     *
     * @param userID the ID of the user
     * @return a list of favorite restaurant response DTOs
     * @throws CustomException if the user is not found or is not a customer
     */
    @Override
    public List<FavoriteRestaurantResponseDTO> getListFavoriteRestaurantOfUser(long userID) throws CustomException {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "user", String.valueOf(userID)));

        if (!user.getRole().equals(Role.CUSTOMER)) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        // Get all favorites where restaurant is APPROVED and user role is CUSTOMER
        List<FavoriteRestaurant> favorites = favoriteRestaurantRepository
                .findByUserAndRestaurantStatusAndUserRole(user, RestaurantStatus.APPROVED, Role.CUSTOMER);

        return favorites.stream()
                .map(fav -> restaurantMapper.toFavoriteRestaurantDTO(fav.getRestaurant()))
                .collect(Collectors.toList());
    }

    /**
     * Adds a restaurant to the user's favorite list.
     *
     * @param userID       the ID of the user
     * @param restaurantID the ID of the restaurant
     * @return the favorite restaurant response DTO
     * @throws CustomException if the user or restaurant is not found, or already favorited
     */
    @Override
    public FavoriteRestaurantResponseDTO addFavoriteRestaurantForUser(long userID, long restaurantID) throws CustomException {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "user", String.valueOf(userID)));

        if (!user.getRole().equals(Role.CUSTOMER)) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantID)));

        // Prevent duplicate favorite
        if (favoriteRestaurantRepository.existsByUserAndRestaurant(user, restaurant)) {
            throw new CustomException(StatusCode.ALREADY_EXISTS);
        }

        // Build DTO and convert to entity for saving
        FavoriteRestaurantDTO favoriteRestaurantDTO = FavoriteRestaurantDTO.builder()
                .user(userID)
                .restaurant(restaurantID)
                .build();

        favoriteRestaurantRepository.save(favoriteRestaurantMapper.toEntity(favoriteRestaurantDTO));

        return restaurantMapper.toFavoriteRestaurantDTO(restaurant);
    }

    /**
     * Removes a restaurant from the user's favorites.
     *
     * @param userID       the ID of the user
     * @param restaurantID the ID of the restaurant
     * @return true if removal was successful, false if not found
     * @throws CustomException if the user or restaurant does not exist, or if unauthorized
     */
    @Override
    public boolean removeFavorite(long userID, long restaurantID) throws CustomException {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "user", userID));

        if (!user.getRole().equals(Role.CUSTOMER)) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", restaurantID));

        // If the favorite does not exist, return false
        if (!favoriteRestaurantRepository.existsByUserAndRestaurant(user, restaurant)) {
            return false;
        }

        // Retrieve the exact favorite entity to delete
        FavoriteRestaurant favorite = favoriteRestaurantRepository.findByUserAndRestaurant(user, restaurant)
                .orElseThrow(() -> new CustomException(
                        StatusCode.NOT_FOUND,
                        "favorite restaurant",
                        String.format("user %s and restaurant %s", user.getFullName(), restaurant.getName())));

        favoriteRestaurantRepository.delete(favorite);
        return true;
    }
}
