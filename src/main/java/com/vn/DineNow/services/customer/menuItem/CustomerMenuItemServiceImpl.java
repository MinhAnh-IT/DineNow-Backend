package com.vn.DineNow.services.customer.menuItem;

import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.MenuItemMapper;
import com.vn.DineNow.payload.response.menuItem.MenuItemResponseDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemSimpleResponseDTO;
import com.vn.DineNow.repositories.FoodCategoryRepository;
import com.vn.DineNow.repositories.MenuItemRepository;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.services.common.fileService.FileService;
import com.vn.DineNow.services.common.cache.RedisService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerMenuItemServiceImpl implements CustomerMenuItemService {
    final UserRepository userRepository;
    final RestaurantRepository restaurantRepository;
    final MenuItemMapper menuItemMapper;
    final FileService fileService;
    final FoodCategoryRepository foodCategoryRepository;
    final MenuItemRepository menuItemRepository;
    final RedisService redisService;

    @Value("${DineNow.key.cache-item}")
    String keyRedis;

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

}
