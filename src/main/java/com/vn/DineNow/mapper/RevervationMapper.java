package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.ReservationDTO;
import com.vn.DineNow.entities.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RevervationMapper {
    @Mapping(target = "reservationPayments", ignore = true)
    @Mapping(target = "reservationOrders", ignore = true)
    @Mapping(source = "customer", target = "customer.id")
    @Mapping(source = "table", target = "table.id")
    @Mapping(source = "restaurant", target = "restaurant.id")
    Reservation toEntity(ReservationDTO reservationDTO);

    @Mapping(source = "customer.id", target = "customer")
    @Mapping(source = "table.id", target = "table")
    @Mapping(source = "restaurant.id", target = "restaurant")
    ReservationDTO toDTO(Reservation reservation);
}
