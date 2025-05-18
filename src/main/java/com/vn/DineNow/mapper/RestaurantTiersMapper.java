package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.RestaurantTiers;
import com.vn.DineNow.payload.request.restaurantTiers.RestaurantTieUpdateRequest;
import com.vn.DineNow.payload.request.restaurantTiers.RestaurantTiersRequest;
import com.vn.DineNow.payload.response.restaurantTiers.RestaurantTierForOwner;
import com.vn.DineNow.payload.response.restaurantTiers.RestaurantTiersResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RestaurantTiersMapper {

    @Mapping(target = "restaurants", ignore = true)
    @Mapping(target = "id", ignore = true)
    RestaurantTiers toEntity(RestaurantTiersRequest request);
    RestaurantTiersResponse toDTO(RestaurantTiers restaurantTiers);

    RestaurantTierForOwner toOwnerDTO(RestaurantTiers restaurantTiers);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurants", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(RestaurantTieUpdateRequest request, @MappingTarget RestaurantTiers restaurantTiers);
}
