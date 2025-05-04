package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.RestaurantTiers;
import com.vn.DineNow.payload.request.restaurantTiers.RestaurantTiersRequest;
import com.vn.DineNow.payload.response.restaurantTiers.RestaurantTiersResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantTiersMapper {

    @Mapping(target = "id", ignore = true)
    RestaurantTiers toEntity(RestaurantTiersRequest request);

    RestaurantTiersResponse toDTO(RestaurantTiers restaurantTiers);
}
