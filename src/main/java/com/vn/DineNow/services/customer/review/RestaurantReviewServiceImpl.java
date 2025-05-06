package com.vn.DineNow.services.customer.review;

import com.vn.DineNow.entities.Reservation;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.ReviewMapper;
import com.vn.DineNow.payload.request.review.ReviewRequestDTO;
import com.vn.DineNow.payload.response.review.ReviewResponse;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.repositories.ReviewRepository;
import com.vn.DineNow.repositories.UserRepository;
import com.vn.DineNow.services.customer.reservation.CustomerReservationService;
import com.vn.DineNow.services.owner.restaurant.OwnerRestaurantService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantReviewServiceImpl implements RestaurantReviewService{
    ReviewRepository reviewRepository;
    CustomerReservationService customerReservationService;
    UserRepository userRepository;
    RestaurantRepository restaurantRepository;
    ReviewMapper reviewMapper;
    OwnerRestaurantService ownerRestaurantService;

    /**
     * Adds a review for a restaurant by a customer.
     *
     * @param customerId the ID of the customer
     * @param restaurantId the ID of the restaurant
     * @param reviewDto the review request DTO
     * @return the added review response
     * @throws CustomException if any error occurs during the process
     */
    @Override
    public ReviewResponse addReview(long customerId, long restaurantId, ReviewRequestDTO reviewDto) throws CustomException {
        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "User", String.valueOf(customerId)));

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Restaurant", String.valueOf(restaurantId)));

        try {
            // Check if the customer has a reservation at the restaurant, and if the reservation does not have a review
            Reservation reservation = customerReservationService
                    .getReservationByCustomerAndRestaurant(customer, restaurant)
                    .orElseThrow(() -> new CustomException(StatusCode.INVALID_REVIEW_ACTION, "restaurant"));

            // save review
            var review = reviewMapper.toEntity(reviewDto);
            review.setCustomer(customer);
            review.setRestaurant(restaurant);
            review.setReservation(reservation);
            reviewRepository.save(review);

            // update avg rating for restaurant
            ownerRestaurantService.updateAvgRatingForRestaurant(restaurant);

            return reviewMapper.toDTO(review);

        } catch (CustomException e) {
            throw e; // giữ nguyên custom exception để trả thông báo đúng
        } catch (Exception e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public List<ReviewResponse> getAllReviewsByRestaurantId(long restaurantId, int page, int size) throws CustomException{
        // Check if the restaurant exists
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "Restaurant", String.valueOf(restaurantId)));

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        var reviews = reviewRepository.findAllByRestaurant(restaurant, pageable);
        return reviews
                .stream()
                .map(reviewMapper::toDTO)
                .toList();
    }
}
