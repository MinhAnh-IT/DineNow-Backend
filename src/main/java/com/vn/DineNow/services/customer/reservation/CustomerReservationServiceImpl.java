package com.vn.DineNow.services.customer.reservation;

import com.vn.DineNow.entities.Reservation;
import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.enums.OrderStatus;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.ReservationMapper;
import com.vn.DineNow.payload.request.reservation.ReservationRequest;
import com.vn.DineNow.repositories.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerReservationServiceImpl implements CustomerReservationService {
    ReservationRepository reservationRepository;
    ReservationMapper reservationMapper;


    /**
     * Creates a new reservation based on the provided reservation request.
     *
     * @param reservationRequest the reservation request containing details
     * @return the created Reservation entity
     * @throws Exception if the reservation request is invalid
     */
    @Override
    @Transactional
    public Reservation createReservation(ReservationRequest reservationRequest) throws Exception {
        // Validate the reservationRequest
        if (reservationRequest.getRestaurantId() == null || reservationRequest.getCustomerId() == null) {
            throw new CustomException(StatusCode.INVALID_INPUT);
        }

        // Map the ReservationRequest to the entity and save it
        var reservation = reservationMapper.toEntity(reservationRequest);
        return reservationRepository.save(reservation);
    }

    /**
     * Retrieves the total number of reservations for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @return the total number of reservations
     */
    @Override
    public long getTotalReservationByRestaurantId(long restaurantId) {
        // Count the total reservations for the restaurant
        return reservationRepository.countByRestaurantId(restaurantId);
    }


    /**
     * Checks if a reservation exists for a specific customer and restaurant.
     *
     * @param customer   the customer making the reservation
     * @param restaurant the restaurant for which the reservation is made
     * @return an Optional containing the Reservation if it exists, otherwise empty
     */
    @Override
    public Optional<Reservation> getReservationByCustomerAndRestaurant(User customer, Restaurant restaurant){
        return reservationRepository.findTopByCustomerAndRestaurantAndReservationOrder_StatusAndReviewIsNull(
                customer, restaurant, OrderStatus.COMPLETED);
    }
}
