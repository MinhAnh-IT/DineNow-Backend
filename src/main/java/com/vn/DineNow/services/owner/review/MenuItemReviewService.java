package com.vn.DineNow.services.owner.review;


import com.vn.DineNow.entities.MenuItemReview;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.MenuItemMapper;
import com.vn.DineNow.mapper.MenuItemReviewMapper;
import com.vn.DineNow.payload.response.review.ReviewResponse;
import com.vn.DineNow.repositories.MenuItemRepository;
import com.vn.DineNow.repositories.MenuItemReviewRepository;
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
public class MenuItemReviewService implements OwnerMenuItemReviewService {
    MenuItemReviewRepository menuItemReviewRepository;
    MenuItemRepository menuItemRepository;
    MenuItemReviewMapper menuItemReviewMapper;

    @Override
    public List<ReviewResponse> getAllReviewsByMenuItemId(long ownerId,long menuItemId, int page, int size) throws CustomException {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        var menuItem = menuItemRepository.findById(menuItemId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "MenuItem", String.valueOf(menuItemId))
        );
        if (!menuItem.getRestaurant().getOwner().getId().equals(ownerId)) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        var reviews = menuItemReviewRepository.findAllByMenuItem(menuItem, pageable);
        return reviews.stream()
                .map(menuItemReviewMapper::toDTO)
                .toList();
    }
}
