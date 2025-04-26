package com.vn.DineNow.services.owner.foodCategory;

import com.vn.DineNow.entities.FoodCategory;
import com.vn.DineNow.entities.MainCategory;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.FoodCategoryMapper;
import com.vn.DineNow.payload.request.foodCategory.FoodCategoryRequest;
import com.vn.DineNow.payload.request.foodCategory.FoodCategoryUpdateRequest;
import com.vn.DineNow.payload.response.foodCategory.FoodCategoryResponseDTO;
import com.vn.DineNow.repositories.FoodCategoryRepository;
import com.vn.DineNow.repositories.MainCategoryRepository;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FoodCategoryServiceImpl implements FoodCategoryService{
    UserRepository userRepository;
    FoodCategoryMapper foodCategoryMapper;
    FoodCategoryRepository foodCategoryRepository;
    RestaurantRepository restaurantRepository;
    MainCategoryRepository mainCategoryRepository;

    /**
     * Adds a new food category for a restaurant owned by the specified owner.
     *
     * @param ownerId       the ID of the owner
     * @param request       the food category request data
     * @return the created food category response
     * @throws CustomException if the owner is not found or any other error occurs
     */
    @Override
    public FoodCategoryResponseDTO addNewFoodCategory(long ownerId, long restaurantId, FoodCategoryRequest request) throws CustomException {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerId)));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId)));

        if (request.getName() != null
                && foodCategoryRepository.existsByNameAndRestaurant_Id(
                        request.getName(), restaurant.getId())) {
            throw new CustomException(StatusCode.EXIST_NAME, request.getName(), "food category");
        }

        MainCategory mainCategory = mainCategoryRepository.findById(request.getMainCategoryId())
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "main category", String.valueOf(request.getMainCategoryId())));
        if (owner.getRole() != Role.OWNER || !owner.getId().equals(restaurant.getOwner().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        var foodCategory = foodCategoryMapper.toEntity(request);
        foodCategory.setRestaurant(restaurant);
        foodCategory.setRestaurant(restaurant);
        foodCategoryRepository.save(foodCategory);
        var response = foodCategoryMapper.toDTO(foodCategory);
        response.setMainCategoryName(mainCategory.getName());

        return response;
    }

    /**
     * Updates an existing food category for a restaurant owned by the specified owner.
     *
     * @param ownerId          the ID of the owner
     * @param foodCategoryId   the ID of the food category to update
     * @param request          the food category update request data
     * @return the updated food category response
     * @throws CustomException if the owner is not found, not authorized, or any other error occurs
     */
    @Override
    public FoodCategoryResponseDTO updateFoodCategory(long ownerId, long foodCategoryId, FoodCategoryUpdateRequest request) throws CustomException {
        FoodCategory foodCategory = foodCategoryRepository.findById(foodCategoryId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "food category", String.valueOf(foodCategoryId)));

        if (request.getMainCategoryId() != null && !mainCategoryRepository.existsById(request.getMainCategoryId())){
            throw new CustomException(StatusCode.NOT_FOUND, "main category", String.valueOf(request.getMainCategoryId()));
        }

        if (!foodCategory.getRestaurant().getOwner().getId().equals(ownerId)){
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        if (request.getName() != null
                && !foodCategory.getName().equals(request.getName())
                && foodCategoryRepository.existsByNameAndRestaurant_Id(
                        request.getName(), foodCategory.getRestaurant().getId())) {
            throw new CustomException(StatusCode.EXIST_NAME, request.getName(), "food category");
        }
        if(request.getMainCategoryId() != null){
            foodCategory.getMainCategory().setId(request.getMainCategoryId());
        }
        foodCategoryMapper.updateEntity(request, foodCategory);

        foodCategoryRepository.save(foodCategory);

        return foodCategoryMapper.toDTO(foodCategory);
    }

    @Override
    public boolean deleteFoodCategory(long ownerId, long foodCategoryId) throws CustomException {
        FoodCategory foodCategory = foodCategoryRepository.findById(foodCategoryId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "food category", String.valueOf(foodCategoryId)));

        if (!foodCategory.getRestaurant().getOwner().getId().equals(ownerId)){
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        try {
            foodCategoryRepository.delete(foodCategory);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(StatusCode.RESOURCE_IN_USE, "food category");
        } catch (Exception e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<FoodCategoryResponseDTO> getAllFoodCategoryForRestaurant(long ownerId, long restaurantId) throws CustomException {
        var restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId)));
        if (!restaurant.getOwner().getId().equals(ownerId)){
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        var foodCategories = foodCategoryRepository.findAllByRestaurant_Id(restaurantId);
        return foodCategories.stream()
                .map(foodCategoryMapper::toDTO)
                .toList();
    }
}
