package com.vn.DineNow.services.customer.reservation;

import com.vn.DineNow.entities.Reservation;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.ReservationMapper;
import com.vn.DineNow.payload.request.Reservation.ReservationRequest;
import com.vn.DineNow.repositories.ReservationRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerReservationServiceImpl implements CustomerReservationService {
    ReservationRepository reservationRepository;
    ReservationMapper reservationMapper;


    @Override
    @Transactional
    public Reservation createReservation(ReservationRequest reservationRequest) throws Exception {
        // Validate the reservationRequest
        if (reservationRequest.getRestaurantId() == null || reservationRequest.getCustomerId() == null) {
            throw new CustomException(StatusCode.INVALID_INPUT);
        }

        // Map the ReservationRequest to the entity and save it
        var reservation = reservationMapper.toEntity(reservationRequest);
        Reservation reservationSaved = reservationRepository.save(reservation);
        return reservationSaved;
    }

}
