package com.vn.DineNow.services.owner.menuItem;

import com.vn.DineNow.entities.*;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.MenuItemMapper;
import com.vn.DineNow.payload.request.menuItem.MenuItemRequestDTO;
import com.vn.DineNow.payload.request.menuItem.MenuItemUpdateDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemResponseDTO;
import com.vn.DineNow.repositories.*;
import com.vn.DineNow.services.common.fileService.FileService;
import com.vn.DineNow.services.common.cache.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Service implementation for restaurant owners to manage menu items,
 * including creation, update, deletion, and availability control.
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OwnerMenuItemServiceImpl implements OwnerMenuItemService {

    final FileService fileService;
    final UserRepository userRepository;
    final RestaurantRepository restaurantRepository;
    final FoodCategoryRepository foodCategoryRepository;
    final MenuItemRepository menuItemRepository;
    final MenuItemMapper menuItemMapper;
    final RedisService redisService;
    final MenuItemReviewRepository reviewRepository;

    @Value("${DineNow.key.cache-item}")
    String keyRedis;

    /**
     * Creates a new menu item for a restaurant owned by a specific owner.
     *
     * @param ownerId             the ID of the owner making the request
     * @param restaurantId        the ID of the restaurant to add the item to
     * @param request             the request containing menu item details
     * @return the created menu item as a DTO
     * @throws CustomException if the owner is not valid, restaurant/category is not found,
     *                         the name already exists, or image upload fails
     */
    @Override
    public MenuItemResponseDTO addNewMenuItem(long ownerId, long restaurantId, MenuItemRequestDTO request) throws CustomException {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerId)));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId)));

        FoodCategory foodCategory = foodCategoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "food category", String.valueOf(request.getCategory())));

        if (!owner.getRole().equals(Role.OWNER) || !restaurant.getOwner().getId().equals(ownerId)) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        request.setName(request.getName().trim());

        if (menuItemRepository.existsByNameAndRestaurant(request.getName(), restaurant)) {
            throw new CustomException(StatusCode.EXIST_NAME, request.getName(), "menu item");
        }

        MenuItem menuItem = menuItemMapper.toEntity(request);
        menuItem.setRestaurant(restaurant);
        menuItem.setCategory(foodCategory);

        try {
            String imageUrl = fileService.uploadFile(request.getImageUrl());
            menuItem.setImageUrl(imageUrl);
        } catch (IOException e) {
            throw new CustomException(StatusCode.IMAGE_UPLOAD_FAILED, request.getImageUrl().getOriginalFilename());
        }

        menuItemRepository.save(menuItem);
        menuItem.setImageUrl(fileService.getPublicFileUrl(menuItem.getImageUrl()));

        return menuItemMapper.toDTO(menuItem);
    }

    /**
     * Updates a menu item by its ID.
     *
     * @param menuItemId          the ID of the menu item to update
     * @param request             the update request containing new values
     * @return the updated menu item as a DTO
     * @throws CustomException if the menu item is not found, or image is invalid or upload fails
     */
    @Override
    public MenuItemResponseDTO updateMenuItem(long ownerId, long menuItemId, MenuItemUpdateDTO request) throws CustomException {
        MenuItem menuItem = menuItemRepository.findByIdAndAvailableTrue(menuItemId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "menu item", String.valueOf(menuItemId)));
        if(menuItem.getRestaurant().getOwner().getId() != ownerId){
            throw new CustomException(StatusCode.FORBIDDEN);
        }
        if (request.getName() != null
                && !request.getName().equals(menuItem.getName())
                && menuItemRepository.existsByNameAndRestaurant(request.getName(), menuItem.getRestaurant())) {
                throw new CustomException(StatusCode.EXIST_NAME,
                        request.getName(),
                        String.format("menu item of %s", menuItem.getRestaurant().getName())
                );
        }

        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            if (!fileService.isValidImage(request.getImageUrl())) {
                throw new CustomException(StatusCode.INVALID_IMAGE_TYPE);
            }
            try {
                String imageUrl = fileService.uploadFile(request.getImageUrl());
                menuItem.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new CustomException(StatusCode.IMAGE_UPLOAD_FAILED, request.getImageUrl().getOriginalFilename());
            }
        }

        menuItemMapper.updateMenuItem(menuItem, request);
        menuItemRepository.save(menuItem);
        menuItem.setImageUrl(fileService.getPublicFileUrl(menuItem.getImageUrl()));

        MenuItemResponseDTO dto = menuItemMapper.toDTO(menuItem);
        String key = keyRedis + menuItemId;

        if (redisService.objectExists(key)) {
            redisService.deleteObject(key);
            redisService.saveObject(key, dto, 20, TimeUnit.MINUTES);
        }

        return dto;
    }

    /**
     * Deletes a menu item from the restaurant if the owner is authorized.
     *
     * @param ownerId     the ID of the owner making the request
     * @param menuItemId  the ID of the menu item to delete
     * @return true if deletion is successful
     * @throws CustomException if the owner or menu item is not found,
     *                         unauthorized, or if image deletion fails
     */
    @Override
    public boolean deleteMenuItem(long ownerId, long menuItemId) throws CustomException {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerId)));

        MenuItem menuItem = menuItemRepository.findByIdAndAvailableTrue(menuItemId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "menu item", String.valueOf(menuItemId)));

        if (!owner.getRole().equals(Role.OWNER) || !owner.getId().equals(menuItem.getRestaurant().getOwner().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        try {
            fileService.deleteFile(menuItem.getImageUrl());
        } catch (IOException e) {
            throw new CustomException(StatusCode.IMAGE_DELETE_FAILED, menuItem.getImageUrl());
        }

        menuItemRepository.delete(menuItem);
        return true;
    }

    /**
     * Retrieves all menu items for a restaurant if the user is its owner.
     *
     * @param ownerId       the ID of the owner
     * @param restaurantId  the ID of the restaurant
     * @return list of all menu items belonging to the restaurant
     * @throws CustomException if the owner or restaurant is not found or unauthorized
     */
    @Override
    public List<MenuItemResponseDTO> GetAllMenuItemForRestaurant(long ownerId, long restaurantId) throws CustomException {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerId)));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId)));

        if (!owner.getRole().equals(Role.OWNER) || !owner.getId().equals(restaurant.getOwner().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        var menuItems = menuItemRepository.findAllByRestaurant(restaurant);

        return menuItems.stream()
                .map(menuItem -> {
                    var dto = menuItemMapper.toDTO(menuItem);
                    dto.setImageUrl(fileService.getPublicFileUrl(dto.getImageUrl()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Updates the availability status of a menu item (e.g., in stock or out of stock).
     *
     * @param ownerId     the ID of the owner making the request
     * @param menuItemId  the ID of the menu item to update
     * @param available   the new availability status
     * @return true if the status was successfully updated
     * @throws CustomException if the owner or menu item is not found,
     *                         unauthorized, or database update fails
     */
    @Override
    public boolean updateMenuItemAvailability(long ownerId, long menuItemId, boolean available) throws CustomException {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerId)));

        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "menu item", String.valueOf(menuItemId)));

        if (!owner.getId().equals(menuItem.getRestaurant().getOwner().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        try {
            menuItem.setAvailable(available);
            menuItemRepository.save(menuItem);
            return true;
        } catch (Exception e) {
            throw new CustomException(StatusCode.BAD_REQUEST);
        }
    }

    @Override
    public void updateAvgRating(MenuItem menuItem) throws CustomException {
        var reviews = reviewRepository.findAllByMenuItem(menuItem);

        if (reviews.isEmpty()) {
            menuItem.setAverageRating(0.0);
        } else {
            double avgRating = reviews.stream()
                    .mapToDouble(MenuItemReview::getRating)
                    .average()
                    .orElse(0.0);

            double rounded = Math.floor(avgRating * 10 + 0.5) / 10.0;

            menuItem.setAverageRating(rounded);
        }
        menuItemRepository.save(menuItem);
    }
}
