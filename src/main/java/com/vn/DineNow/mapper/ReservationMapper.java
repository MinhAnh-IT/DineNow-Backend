package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.Reservation;
import com.vn.DineNow.payload.request.Order.OrderRequest;
import com.vn.DineNow.payload.request.Reservation.ReservationRequest;
import com.vn.DineNow.payload.response.reservation.ReservationDetailsResponse;
import com.vn.DineNow.payload.response.reservation.ReservationResponse;
import com.vn.DineNow.payload.response.reservation.ReservationSimpleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "restaurant.id", source = "restaurantId")
    @Mapping(target = "reservationPayments", ignore = true)
    @Mapping(target = "reservationOrders", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer.id", source = "customerId")
    @Mapping(target = "createdAt", ignore = true)
    Reservation toEntity(ReservationRequest reservationRequest);

    @Mapping(target = "restaurantId", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    ReservationRequest toRequestDTO(OrderRequest request);

    @Mapping(target = "restaurantName", source = "restaurant.name")
    @Mapping(target = "restaurantAddress", source = "restaurant.address")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "customerEmail", source = "customer.email")
    ReservationDetailsResponse toDetailsDTO(Reservation reservation);

    @Mapping(target = "restaurantName", source = "restaurant.name")
    ReservationSimpleResponse toSimpleDTO(Reservation reservation);


    @Mapping(target = "restaurantName", source = "restaurant.name")
    @Mapping(target = "customerName", source = "customer.fullName")
    ReservationResponse toResponseDTO(Reservation reservation);

}
