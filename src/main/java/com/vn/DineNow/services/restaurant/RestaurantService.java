package com.vn.DineNow.services.restaurant;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.RestaurantStatus;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.RestaurantMapper;
import com.vn.DineNow.payload.request.restaurant.RestaurantRequestDTO;
import com.vn.DineNow.payload.request.restaurant.RestaurantUpdateDTO;
import com.vn.DineNow.payload.request.restaurant.SearchRestaurantDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantResponseDTO;
import com.vn.DineNow.payload.response.restaurant.RestaurantSimpleResponseDTO;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.repositories.RestaurantTypeRepository;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.services.redis.IRedisService;
import com.vn.DineNow.services.restaurantImages.IRestaurantImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantService implements IRestaurantService{
    final UserRepository userRepository;
    final RestaurantRepository restaurantRepository;
    final IRestaurantImageService restaurantImageService;
    final RestaurantTypeRepository restaurantTypeRepository;
    final RestaurantMapper restaurantMapper;
    final IRedisService redisService;

    @Value("${DineNow.key.cache-restaurant}")
    String keyRedis;


    @Override
    public RestaurantResponseDTO createRestaurant(long ownerID, RestaurantRequestDTO requestDTO) throws CustomException {
        User owner = userRepository.findById(ownerID)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerID)));

        if (!owner.getRole().equals(Role.OWNER)) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        if (restaurantRepository.existsByNameAndOwner(requestDTO.getName(), owner)) {
            throw new CustomException(StatusCode.EXIST_NAME, "Restaurant", "owner");
        }

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
        responseDTO.setImageUrls(restaurantImageService.getImageUrlsByRestaurantId(savedRestaurant.getId()));

        return responseDTO;
    }

    @Override
    public Page<RestaurantSimpleResponseDTO> getAllRestaurant(int page, int size) {
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

    @Override
    public RestaurantResponseDTO updateRestaurant(long owner, long restaurantId, RestaurantUpdateDTO restaurantUpdateDTO)
            throws CustomException {
        // Check if the restaurant exists and the user is the owner
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId)));
        if(restaurant.getOwner().getId() != owner){
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        // Update basic fields from request DTO
        restaurantMapper.updateRestaurantFromRequest(restaurantUpdateDTO, restaurant);

        // Update restaurant type if a new typeId is provided
        if (restaurantUpdateDTO.getTypeId() != null) {
            restaurant.setType(
                    restaurantTypeRepository.findById(restaurantUpdateDTO.getTypeId())
                            .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant type", String.valueOf(restaurantUpdateDTO.getTypeId())))
            );
        }

        // Replace restaurant images if a new image list is provided
        for (MultipartFile image : restaurantUpdateDTO.getImages()){
            restaurantImageService.validateImageFile(image);
        }
        if (restaurantUpdateDTO.getImages() != null){
            restaurantImageService.deleteImagesByRestaurantId(restaurantId);
            restaurantImageService.saveImages(restaurantId, restaurantUpdateDTO.getImages());
        }

        // Save the updated restaurant to the database
        restaurantRepository.save(restaurant);

        // Convert to DTO and add image URLs
        RestaurantResponseDTO responseDTO = restaurantMapper.toDTO(restaurant);
        responseDTO.setImageUrls(restaurantImageService.getImageUrlsByRestaurantId(restaurant.getId()));

        // Update restaurant info in Redis cache
        String key = keyRedis + String.valueOf(restaurantId);
        if (redisService.objectExists(key)){
            redisService.deleteObject(key);
            redisService.saveObject(key, responseDTO, 20, TimeUnit.MINUTES);
        }

        return responseDTO;
    }
}
