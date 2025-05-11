package com.vn.DineNow.services.owner.restaurant;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.RestaurantTiers;
import com.vn.DineNow.entities.Review;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.RestaurantStatus;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.RestaurantMapper;
import com.vn.DineNow.payload.request.restaurant.RestaurantRequestDTO;
import com.vn.DineNow.payload.request.restaurant.RestaurantUpdateDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.repositories.*;
import com.vn.DineNow.services.common.cache.RedisService;
import com.vn.DineNow.services.owner.restaurant.restaurantImages.RestaurantImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service implementation for restaurant owners to manage their restaurants,
 * including creation, update, and retrieval of their owned restaurants.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OwnerRestaurantServiceImpl implements OwnerRestaurantService {

    final RestaurantRepository restaurantRepository;
    final RestaurantMapper restaurantMapper;
    final UserRepository userRepository;
    final RestaurantImageService restaurantImageService;
    final RestaurantTypeRepository restaurantTypeRepository;
    final RedisService redisService;
    final RestaurantTierRepository restaurantTierRepository;
    final ReviewRepository reviewRepository;

    @Value("${DineNow.key.cache-restaurant}")
    String keyRedis;

    /**
     * Creates a new restaurant associated with the given owner.
     *
     * @param ownerID    the ID of the owner creating the restaurant
     * @param requestDTO the restaurant creation request data
     * @return the newly created restaurant as a DTO
     * @throws CustomException if the owner is not found, not authorized, the name already exists,
     *                         the restaurant type does not exist, or image saving fails
     */
    @Override
    public RestaurantResponseDTO createRestaurant(long ownerID, RestaurantRequestDTO requestDTO) throws CustomException {
        User owner = userRepository.findById(ownerID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerID)));

        if (!owner.getRole().equals(Role.OWNER)) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        if (restaurantRepository.existsByNameAndOwner(requestDTO.getName(), owner)) {
            throw new CustomException(StatusCode.EXIST_NAME, requestDTO.getName(), String.format("restaurants of %s", owner.getFullName()));
        }
        var restaurantTier = restaurantTierRepository.findById(requestDTO.getRestaurantTierId())
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant tier", String.valueOf(requestDTO.getRestaurantTierId())));

        Restaurant restaurant = restaurantMapper.toEntity(requestDTO);
        restaurant.setOwner(owner);

        restaurant.setType(
                restaurantTypeRepository.findById(requestDTO.getTypeId())
                        .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant type", String.valueOf(requestDTO.getTypeId())))
        );

        restaurantRepository.save(restaurant);

        if (requestDTO.getImages() != null && !requestDTO.getImages().isEmpty()) {
            restaurantImageService.saveImages(restaurant.getId(), requestDTO.getImages());
        }

        Restaurant savedRestaurant = restaurantRepository.findById(restaurant.getId())
                .orElseThrow(() -> new CustomException(StatusCode.RESTAURANT_NOT_FOUND, restaurant.getId()));

        RestaurantResponseDTO responseDTO = restaurantMapper.toDTO(savedRestaurant);
        responseDTO.setRestaurantTierName(restaurantTier.getName());
        responseDTO.setImageUrls(restaurantImageService.getImageUrlsByRestaurantId(savedRestaurant.getId()));

        return responseDTO;
    }

    /**
     * Updates the information of an existing restaurant owned by the given owner.
     *
     * @param owner               the ID of the owner making the update
     * @param restaurantId        the ID of the restaurant to update
     * @param restaurantUpdateDTO the update request containing new values
     * @return the updated restaurant as a response DTO
     * @throws CustomException if the restaurant or type is not found, unauthorized, or image validation fails
     */
    @Override
    public RestaurantResponseDTO updateRestaurant(long owner, long restaurantId, RestaurantUpdateDTO restaurantUpdateDTO)
            throws CustomException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId)));

        if (restaurant.getOwner().getId() != owner) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        if(restaurantUpdateDTO.getName() != null
            && !restaurant.getName().equals(restaurantUpdateDTO.getName())
            && restaurantRepository.existsByNameAndOwner(restaurantUpdateDTO.getName(), restaurant.getOwner())){
            throw new CustomException(StatusCode.EXIST_NAME, restaurantUpdateDTO.getName(), String.format("restaurants of %s", restaurant.getOwner().getFullName()));
        }

        restaurantMapper.updateRestaurantFromRequest(restaurantUpdateDTO, restaurant);

        if (restaurantUpdateDTO.getTypeId() != null) {
            restaurant.setType(
                    restaurantTypeRepository.findById(restaurantUpdateDTO.getTypeId())
                            .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant type", String.valueOf(restaurantUpdateDTO.getTypeId())))
            );
        }

        // Validate images before saving
        for (MultipartFile image : restaurantUpdateDTO.getImages()) {
            restaurantImageService.validateImageFile(image);
        }

        if (restaurantUpdateDTO.getImages() != null) {
            restaurantImageService.deleteImagesByRestaurantId(restaurantId);
            restaurantImageService.saveImages(restaurantId, restaurantUpdateDTO.getImages());
        }

        restaurantRepository.save(restaurant);

        RestaurantResponseDTO responseDTO = restaurantMapper.toDTO(restaurant);
        responseDTO.setImageUrls(restaurantImageService.getImageUrlsByRestaurantId(restaurant.getId()));

        // Update cached data in Redis
        String key = keyRedis + restaurantId;
        if (redisService.objectExists(key)) {
            redisService.deleteObject(key);
            redisService.saveObject(key, responseDTO, 20, TimeUnit.MINUTES);
        }

        return responseDTO;
    }

    /**
     * Retrieves all restaurants owned by the specified owner.
     *
     * @param ownerId the ID of the owner
     * @return list of simplified restaurant response DTOs
     * @throws CustomException if the owner is not found
     */
    @Override
    public List<RestaurantSimpleResponseDTO> getRestaurantByOwner(long ownerId) throws CustomException {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerId)));

        var restaurants = restaurantRepository.findAllByOwner(owner);

        return restaurants.stream()
                .map(restaurantMapper::toSimpleDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates the status of a restaurant owned by the specified owner.
     *
     * @param ownerId      the ID of the owner
     * @param restaurantId the ID of the restaurant to update
     * @param status       the new status to set
     * @return true if the operation was successful; false otherwise
     * @throws CustomException if the restaurant is not found, unauthorized, or if the status transition is not allowed
     */
    @Override
    public boolean updateRestaurantStatus(long ownerId, long restaurantId, RestaurantStatus status) throws CustomException {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId)));
        if (restaurant.getOwner().getId() != ownerId) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }
        if (isTransitionAllowed(restaurant.getStatus(), status)) {
            restaurant.setStatus(status);
            restaurantRepository.save(restaurant);
            return true;
        }
        return false;
    }

    /**
     * Updates the average rating for a restaurant based on its reviews.
     *
     * @param restaurant the restaurant to update
     * @throws CustomException if any error occurs during the process
     */
    @Override
    public void updateAvgRatingForRestaurant(Restaurant restaurant) throws CustomException {
        var reviews = reviewRepository.findAllByRestaurant(restaurant);

        if (reviews.isEmpty()) {
            restaurant.setAverageRating(0.0);
        } else {
            double avgRating = reviews.stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(0.0);

            double rounded = Math.floor(avgRating * 10 + 0.5) / 10.0;

            restaurant.setAverageRating(rounded);
        }
        restaurantRepository.save(restaurant);
    }


    /**
     * Checks if the transition between two restaurant statuses is allowed.
     *
     * @param current the current status of the restaurant
     * @param target  the target status to transition to
     * @return true if the transition is allowed; false otherwise
     */
    private boolean isTransitionAllowed(RestaurantStatus current, RestaurantStatus target) {
        return switch (current) {
            case APPROVED -> target == RestaurantStatus.SUSPENDED;
            case SUSPENDED -> target == RestaurantStatus.APPROVED;
            default -> false;
        };
    }
}