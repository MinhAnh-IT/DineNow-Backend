package com.vn.DineNow.services.owner.review;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.ReviewMapper;
import com.vn.DineNow.payload.response.review.ReviewResponse;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.repositories.ReviewRepository;
import com.vn.DineNow.repositories.UserRepository;
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
public class RestaurantReviewService implements OwnerRestaurantReviewService {
    RestaurantRepository restaurantRepository;
    ReviewRepository restaurantReviewRepository;
    ReviewMapper restaurantReviewMapper;


    @Override
    public List<ReviewResponse> getAllReviewsByRestaurantId(long ownerId, long restaurantId, int page, int size) throws CustomException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Restaurant", String.valueOf(restaurantId)));

        if (!restaurant.getOwner().getId().equals(ownerId)) {
            throw new CustomException(StatusCode.FORBIDDEN);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        var reviews = restaurantReviewRepository.findAllByRestaurant(restaurant, pageable);
        return reviews.stream()
                .map(restaurantReviewMapper::toDTO)
                .toList();
    }
}
