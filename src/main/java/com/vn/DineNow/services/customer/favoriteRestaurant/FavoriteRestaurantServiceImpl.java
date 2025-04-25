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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavoriteRestaurantServiceImpl implements FavoriteRestaurantService {
    FavoriteRestaurantRepository favoriteRestaurantRepository;
    UserRepository userRepository;
    RestaurantMapper restaurantMapper;
    FavoriteRestaurantMapper favoriteRestaurantMapper;
    RestaurantRepository restaurantRepository;

    @Override
    public List<FavoriteRestaurantResponseDTO> getListFavoriteRestaurantOfUser(long userID) throws CustomException {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));
        if (!user.getRole().equals(Role.CUSTOMER)) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }
        List<FavoriteRestaurant> favorites = favoriteRestaurantRepository
                .findByUserAndRestaurantStatusAndUserRole(user, RestaurantStatus.APPROVED, Role.CUSTOMER);

        return favorites.stream()
                .map(fav -> restaurantMapper.toFavoriteRestaurantDTO(fav.getRestaurant()))
                .collect(Collectors.toList());
    }

    @Override
    public FavoriteRestaurantResponseDTO addFavoriteRestaurantForUser(long userID, long restaurantID) throws CustomException {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "user", String.valueOf(userID)));
        if (!user.getRole().equals(Role.CUSTOMER)){
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantID)));

        if (favoriteRestaurantRepository.existsByUserAndRestaurant(user, restaurant)){
            throw new CustomException(StatusCode.ALREADY_EXISTS);
        }

        FavoriteRestaurantDTO favoriteRestaurantDTO = FavoriteRestaurantDTO.builder()
                .user(userID)
                .restaurant(restaurantID)
                .build();
        favoriteRestaurantRepository.save(favoriteRestaurantMapper.toEntity(favoriteRestaurantDTO));

        return restaurantMapper.toFavoriteRestaurantDTO(restaurant);
    }

    @Override
    public boolean removeFavorite(long userID, long restaurantID) throws CustomException {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "user", userID));

        if (!user.getRole().equals(Role.CUSTOMER)){
            throw new CustomException(StatusCode.FORBIDDEN);
        }
        Restaurant restaurant = restaurantRepository.findById(restaurantID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", restaurantID));

        if (!favoriteRestaurantRepository.existsByUserAndRestaurant(user, restaurant)) {
            return false;
        }

        FavoriteRestaurant favorite = favoriteRestaurantRepository.findByUserAndRestaurant(user, restaurant)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND));

        favoriteRestaurantRepository.delete(favorite);
        return true;
    }

}
