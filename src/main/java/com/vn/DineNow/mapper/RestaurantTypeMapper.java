package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.RestaurantType;
import com.vn.DineNow.payload.request.restaurantType.RestaurantTypeRequest;
import com.vn.DineNow.payload.request.restaurantType.RestaurantTypeUpdateRequest;
import com.vn.DineNow.payload.response.restaurantType.RestaurantTypeResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RestaurantTypeMapper {

    RestaurantTypeResponse toDTO(RestaurantType restaurantType);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurants", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    RestaurantType toEntity(RestaurantTypeRequest restaurantTypeDTO);

    @Mapping(target = "restaurants", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void UpdateRestaurantTypeFromRequest(@MappingTarget RestaurantType restaurantType, RestaurantTypeUpdateRequest request);

}
