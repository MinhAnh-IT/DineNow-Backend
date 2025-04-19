package com.vn.DineNow.services.menuItem;

import com.vn.DineNow.entities.FoodCategory;
import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.Role;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.MenuItemMapper;
import com.vn.DineNow.payload.request.menuItem.MenuItemRequestDTO;
import com.vn.DineNow.payload.request.menuItem.MenuItemUpdateDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemResponseDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemSimpleResponseDTO;
import com.vn.DineNow.repositories.FoodCategoryRepository;
import com.vn.DineNow.repositories.MenuItemRepository;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.services.fileService.IFileService;
import com.vn.DineNow.services.redis.IRedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuItemService implements IMenuItemService{
    final UserRepository userRepository;
    final RestaurantRepository restaurantRepository;
    final MenuItemMapper menuItemMapper;
    final IFileService fileService;
    final FoodCategoryRepository foodCategoryRepository;
    final MenuItemRepository menuItemRepository;
    final IRedisService redisService;

    @Value("${DineNow.key.cache-item}")
    String keyRedis;

    @Override
    public MenuItemResponseDTO addNewMenuItem(long ownerId, long restaurantId, MenuItemRequestDTO menuItemRequestDTO) throws  CustomException{

        // Validate and fetch the owner, restaurant, and food category
        User owner =userRepository.findById(ownerId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerId))
        );

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId))
        );

        FoodCategory foodCategory = foodCategoryRepository.findById(menuItemRequestDTO.getCategory()).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "food category", String.valueOf(menuItemRequestDTO.getCategory()))
        );

        if (!owner.getRole().equals(Role.OWNER) || !restaurant.getOwner().getId().equals(ownerId)){
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        // validation input
        menuItemRequestDTO.setName(menuItemRequestDTO.getName().trim());

        if (menuItemRepository.existsByNameAndRestaurant(menuItemRequestDTO.getName(), restaurant)){
            throw new CustomException(StatusCode.EXIST_NAME, "Food", "restaurant");
        }
        MenuItem menuItem = menuItemMapper.toEntity(menuItemRequestDTO);
        menuItem.setRestaurant(restaurant);
        menuItem.setCategory(foodCategory);
        try {
            String imageUrl = fileService.uploadFile(menuItemRequestDTO.getImageUrl());
            menuItem.setImageUrl(imageUrl);
        } catch (IOException e) {
            throw new CustomException(StatusCode.IMAGE_UPLOAD_FAILED, menuItemRequestDTO.getImageUrl().getOriginalFilename());
        }

        menuItemRepository.save(menuItem);
        menuItem.setImageUrl(fileService.getPublicFileUrl(menuItem.getImageUrl()));
        return menuItemMapper.toDTO(menuItem);
    }

    @Override
    public List<MenuItemSimpleResponseDTO> GetAllSimpleMenuItemAvailableForRestaurant(long restaurantId) throws CustomException{
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId))
        );

        var simpleMenuItem = menuItemRepository.findAllByRestaurantAndAvailableTrue(restaurant);
        return simpleMenuItem.stream()
                .map(menuItem -> {
                    var dto = menuItemMapper.toSimpleDTO(menuItem);
                    dto.setImageUrl(fileService.getPublicFileUrl(dto.getImageUrl()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<MenuItemSimpleResponseDTO> GetAllMenuAvailableTrue(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var menuItems = menuItemRepository.findAllByAvailableTrue(pageable);

        return menuItems.map(menuItem -> {
            var dto = menuItemMapper.toSimpleDTO(menuItem);
            dto.setImageUrl(fileService.getPublicFileUrl(dto.getImageUrl()));
            return dto;
        });
    }

    @Override
    public MenuItemResponseDTO updateMenuItem(long menuItemId, MenuItemUpdateDTO menuItemUpdateDTO) throws CustomException{
        MenuItem menuItem = menuItemRepository.findByIdAndAvailableTrue(menuItemId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "menu item", String.valueOf(menuItemId))
        );

        if (menuItemUpdateDTO.getImageUrl() != null && !menuItemUpdateDTO.getImageUrl().isEmpty()) {
            if (!fileService.isValidImage(menuItemUpdateDTO.getImageUrl())) {
                throw new CustomException(StatusCode.INVALID_IMAGE_TYPE);
            }
            try {
                String imageUrl = fileService.uploadFile(menuItemUpdateDTO.getImageUrl());
                menuItem.setImageUrl(imageUrl);
            } catch (IOException e) {
                throw new CustomException(StatusCode.IMAGE_UPLOAD_FAILED, menuItemUpdateDTO.getImageUrl().getOriginalFilename());
            }
        }

        menuItemMapper.updateMenuItem(menuItem, menuItemUpdateDTO);

        menuItemRepository.save(menuItem);
        menuItem.setImageUrl(fileService.getPublicFileUrl(menuItem.getImageUrl()));
        MenuItemResponseDTO dto = menuItemMapper.toDTO(menuItem);
        // Save DTO to Redis with TTL 20 minutes
        String key = keyRedis + String.valueOf(menuItemId);
        if (redisService.objectExists(key)){
            redisService.deleteObject(key);
            redisService.saveObject(key, dto, 20, TimeUnit.MINUTES);
        }
        return menuItemMapper.toDTO(menuItem);
    }

    @Override
    public boolean deleteMenuItem(long ownerId, long menuItemId) throws CustomException {
        User owner = userRepository.findById(ownerId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerId))
        );

        MenuItem menuItem = menuItemRepository.findByIdAndAvailableTrue(menuItemId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "menu item", String.valueOf(menuItemId))
        );
        if (!owner.getRole().equals(Role.OWNER) || !owner.getId().equals(menuItem.getRestaurant().getOwner().getId())){
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

    @Override
    public MenuItemResponseDTO getMenuItemById(long menuId) throws CustomException {
        String key = keyRedis + menuId;
        // check from redis cache
        var cachedDTO = redisService.getObject(key, MenuItemResponseDTO.class);
        if (cachedDTO != null){
            return cachedDTO;
        }

        // Fetch from DB if not found in cache
        MenuItem menuItem = menuItemRepository.findByIdAndAvailableTrue(menuId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "menu item", String.valueOf(menuId)));

        // Convert to DTO
        var dto = menuItemMapper.toDTO(menuItem);
        dto.setImageUrl(fileService.getPublicFileUrl(dto.getImageUrl()));

        // Save DTO to Redis with TTL 20 minutes
        redisService.saveObject(key, dto, 20, TimeUnit.MINUTES);
        return dto;
    }

    @Override
    public List<MenuItemResponseDTO> GetAllMenuItemForRestaurant(long ownerId, long restaurantId) throws CustomException {
        User owner = userRepository.findById(ownerId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerId))
        );

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId))
        );
        if(!owner.getRole().equals(Role.OWNER) || !owner.getId().equals(restaurant.getOwner().getId())){
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
     * Updates the availability status of a menu item.
     *
     * @param ownerId     the ID of the restaurant owner making the request
     * @param menuItemId  the ID of the menu item to update
     * @param available   the new availability status to set
     * @return true if the update is successful
     * @throws CustomException if the owner or menu item is not found,
     *                         if the owner is not authorized,
     *                         or if an error occurs during the update
     */
    @Override
    public boolean updateMenuItemAvailability(long ownerId, long menuItemId, boolean available) throws CustomException {
        // Check if the owner exists
        User owner = userRepository.findById(ownerId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "owner", String.valueOf(ownerId))
        );

        // Check if the menu item exists
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "menu item", String.valueOf(menuItemId))
        );

        // Ensure the owner is authorized to update this menu item
        if (!owner.getId().equals(menuItem.getRestaurant().getOwner().getId())) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        // Attempt to update the availability status
        try {
            menuItem.setAvailable(available);
            menuItemRepository.save(menuItem);
            return true;
        } catch (Exception e) {
            // Catch any unexpected errors during the update
            throw new CustomException(StatusCode.BAD_REQUEST);
        }
    }
}
