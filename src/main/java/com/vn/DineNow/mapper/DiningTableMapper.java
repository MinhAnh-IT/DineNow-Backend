package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.DiningTableDTO;
import com.vn.DineNow.entities.DiningTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DiningTableMapper {

    @Mapping(target = "tableReservations", ignore = true)
    @Mapping(source = "restaurant", target = "restaurant.id")
    DiningTable toEntity(DiningTableDTO diningTableDTO);

    @Mapping(source = "restaurant.id", target = "restaurant")
    DiningTableDTO toDTO(DiningTable diningTable);

}
