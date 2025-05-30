package com.vn.DineNow.services.customer.menuItem;

import com.vn.DineNow.entities.*;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.MenuItemMapper;
import com.vn.DineNow.payload.request.menuItem.MenuItemFilterRequest;
import com.vn.DineNow.payload.response.menuItem.MenuItemResponseDTO;
import com.vn.DineNow.payload.response.menuItem.MenuItemSimpleResponseDTO;
import com.vn.DineNow.repositories.*;
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

/**
 * Service implementation for retrieving menu items available to customers.
 * Includes Redis caching for menu item detail retrieval.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerMenuItemServiceImpl implements CustomerMenuItemService {
    final RestaurantRepository restaurantRepository;
    final MenuItemMapper menuItemMapper;
    final FileService fileService;
    final FoodCategoryRepository foodCategoryRepository;
    final MainCategoryRepository mainCategoryRepository;
    final MenuItemRepository menuItemRepository;
    final RedisService redisService;
    final RestaurantTypeRepository restaurantTypeRepository;

    @Value("${DineNow.key.cache-item}")
    String keyRedis;

    /**
     * Retrieves all available menu items for a specific restaurant in simple format.
     *
     * @param restaurantId the ID of the restaurant
     * @return list of simple menu item DTOs
     * @throws CustomException if restaurant is not found
     */
    @Override
    public List<MenuItemSimpleResponseDTO> GetAllSimpleMenuItemAvailableForRestaurant(long restaurantId) throws CustomException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "restaurant", String.valueOf(restaurantId))
        );

        var simpleMenuItem = menuItemRepository.findAllByRestaurantAndAvailableTrue(restaurant);

        return simpleMenuItem.stream()
                .map(menuItem -> {
                    var dto = menuItemMapper.toSimpleDTO(menuItem);
                    // Convert stored image filename to public URL
                    dto.setImageUrl(fileService.getPublicFileUrl(dto.getImageUrl()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves paginated list of all available menu items.
     *
     * @param page page number (zero-based)
     * @param size number of records per page
     * @return page of simple menu item DTOs
     */
    @Override
    public Page<MenuItemSimpleResponseDTO> GetAllMenuAvailableTrue(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var menuItems = menuItemRepository.findAllByAvailableTrue(pageable);

        return menuItems.map(menuItem -> {
            var dto = menuItemMapper.toSimpleDTO(menuItem);
            // Convert stored image filename to public URL
            dto.setImageUrl(fileService.getPublicFileUrl(dto.getImageUrl()));
            return dto;
        });
    }

    /**
     * Retrieves a single menu item by ID.
     * Uses Redis caching to improve performance.
     *
     * @param menuId the ID of the menu item
     * @return menu item DTO
     * @throws CustomException if menu item is not found or unavailable
     */
    @Override
    public MenuItemResponseDTO getMenuItemById(long menuId) throws CustomException {
        String key = keyRedis + menuId;

        // Check Redis cache first
        var cachedDTO = redisService.getObject(key, MenuItemResponseDTO.class);
        if (cachedDTO != null) {
            return cachedDTO;
        }

        // Fallback to DB query if cache miss
        MenuItem menuItem = menuItemRepository.findByIdAndAvailableTrue(menuId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "menu item", String.valueOf(menuId)));

        // Convert to DTO
        var dto = menuItemMapper.toDTO(menuItem);
        dto.setImageUrl(fileService.getPublicFileUrl(dto.getImageUrl()));

        // Save DTO into Redis with 20-minute TTL
        redisService.saveObject(key, dto, 20, TimeUnit.MINUTES);

        return dto;
    }

    /**
     * Retrieves all available menu items for a specific main category.
     *
     * @param mainCategoryId the ID of the main category
     * @param page page number (zero-based)
     * @param size number of records per page
     * @return list of simple menu item DTOs
     * @throws CustomException if main category is not found
     */
    @Override
    public List<MenuItemSimpleResponseDTO> GetAllMenuItemAvailableTrueByMainCategory(long mainCategoryId, int page, int size)
            throws CustomException {
        var mainCategory = mainCategoryRepository.findById(mainCategoryId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "main category", String.valueOf(mainCategoryId)));
        Pageable pageable = PageRequest.of(page, size);
        var menuItems = menuItemRepository.findAllByCategory_MainCategoryAndAvailableTrue(mainCategory, pageable);

        return menuItems.stream()
                .map(menuItem -> {
                    var dto = menuItemMapper.toSimpleDTO(menuItem);
                    dto.setImageUrl(fileService.getPublicFileUrl(dto.getImageUrl()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all available menu items for a specific food category.
     *
     * @param categoryId the ID of the food category
     * @param page page number (zero-based)
     * @param size number of records per page
     * @return list of simple menu item DTOs
     * @throws CustomException if food category is not found
     */
    @Override
    public List<MenuItemSimpleResponseDTO> GetAllMenuItemAvailableTrueByCategory(long categoryId, int page, int size) throws CustomException {
        var category = foodCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "category", String.valueOf(categoryId)));
        Pageable pageable = PageRequest.of(page, size);
        var menuItems = menuItemRepository.findAllByCategoryAndAvailableTrue(category, pageable);
        return menuItems.stream()
                .map(menuItem -> {
                    var dto = menuItemMapper.toSimpleDTO(menuItem);
                    dto.setImageUrl(fileService.getPublicFileUrl(dto.getImageUrl()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of featured menu items.
     *
     * @return list of simple menu item DTOs
     * @throws CustomException if any error occurs
     */
    @Override
    public List<MenuItemSimpleResponseDTO> getListMenuItemFeatured() throws CustomException {
        var menuItems = menuItemRepository.findTopFeaturedMenuItems();
        return menuItems.stream()
                .map(menuItem -> {
                    var dto = menuItemMapper.toSimpleDTO(menuItem);
                    dto.setImageUrl(fileService.getPublicFileUrl(dto.getImageUrl()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all menu items based on various filters.
     *
     * @param request the filter request
     * @param page page number (zero-based)
     * @param size number of records per page
     * @return list of simple menu item DTOs
     * @throws CustomException if any error occurs
     */
    @Override
    public List<MenuItemSimpleResponseDTO> getAllMenuItemByFilter(MenuItemFilterRequest request, int page, int size) throws CustomException {
        if(request.getMinPrice() != null && request.getMaxPrice() != null && request.getMinPrice().longValue() > request.getMaxPrice().longValue()) {
            throw new CustomException(StatusCode.INVALID_INPUT);
        }

        RestaurantType restaurantType = request.getRestaurantTypeId() == null ? null :
                restaurantTypeRepository.findById(request.getRestaurantTypeId())
                        .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant type", String.valueOf(request.getRestaurantTypeId())));

        MainCategory mainCategory = request.getMainCategoryId() == null ? null :
                mainCategoryRepository.findById(request.getMainCategoryId())
                        .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "main category", String.valueOf(request.getMainCategoryId())));

        Pageable pageable = PageRequest.of(page, size);
        var menuItems = menuItemRepository.findAllMenuItemByFilter(
                request.getCity(),
                restaurantType,
                mainCategory,
                request.getMinPrice(),
                request.getMaxPrice(),
                pageable
        );

        return menuItems.stream()
                .map(menuItemMapper::toSimpleDTO)
                .toList();
    }
}
