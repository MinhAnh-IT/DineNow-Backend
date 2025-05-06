package com.vn.DineNow.services.customer.review;

import com.vn.DineNow.entities.MenuItem;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.MenuItemReviewMapper;
import com.vn.DineNow.payload.request.review.ReviewRequestDTO;
import com.vn.DineNow.payload.response.review.ReviewResponse;
import com.vn.DineNow.repositories.MenuItemRepository;
import com.vn.DineNow.repositories.MenuItemReviewRepository;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.services.customer.menuItem.CustomerMenuItemService;
import com.vn.DineNow.services.customer.orderItem.CustomerOrderItemService;
import com.vn.DineNow.services.owner.menuItem.OwnerMenuItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MenuItemReviewServiceImpl implements MenuItemReviewService{
    UserRepository userRepository;
    MenuItemRepository menuItemRepository;
    MenuItemReviewRepository menuItemReviewRepository;
    CustomerOrderItemService orderItemService;
    MenuItemReviewMapper menuItemReviewMapper;
    OwnerMenuItemService menuItemService;

    /**
     * Adds a review for a menu item by a customer.
     *
     * @param customerId the ID of the customer
     * @param menuItemId the ID of the menu item
     * @param reviewDto the review request DTO
     * @return the added review response
     * @throws CustomException if any error occurs during the process
     */
    @Override
    public ReviewResponse addReview(long customerId, long menuItemId, ReviewRequestDTO reviewDto) throws CustomException {
        var user = userRepository.findById(customerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "User", String.valueOf(customerId)));

        var menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "MenuItem", String.valueOf(menuItemId)));

        if (!menuItem.getAvailable()){
            throw new CustomException(StatusCode.INVALID_ENTITY, "MenuItem is not available");
        }
        // Check if user has ordered the item and not reviewed yet
        var orderItem = orderItemService.getOrderItemIfCustomerAteButNotReviewed(user, menuItem)
                .orElseThrow(() -> new CustomException(StatusCode.INVALID_REVIEW_ACTION, "menuItem"));

        try {
            // Create and save review
            var review = menuItemReviewMapper.toEntity(reviewDto);
            review.setUser(user);
            review.setMenuItem(menuItem);
            review.setOrderItem(orderItem);

            menuItemReviewRepository.save(review);

            // update average rating of menu item
            menuItemService.updateAvgRating(menuItem);
            return menuItemReviewMapper.toDTO(review);
        } catch (Exception e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Retrieves all reviews for a menu item.
     *
     * @param menuItemId the ID of the menu item
     * @param page the page number
     * @param size the page size
     * @return a list of review responses
     * @throws CustomException if any error occurs during the process
     */
    @Override
    public List<ReviewResponse> getAllReviewsByMenuItemId(long menuItemId, int page, int size) throws CustomException{
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        var menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "MenuItem", String.valueOf(menuItemId)));
        if (!menuItem.getAvailable()){
            throw new CustomException(StatusCode.INVALID_ENTITY, "MenuItem is not available");
        }

        var reviews = menuItemReviewRepository.findAllByMenuItem(menuItem, pageable);
        return reviews.stream()
                .map(menuItemReviewMapper::toDTO)
                .toList();
    }

}
