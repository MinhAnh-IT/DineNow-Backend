package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "userFavoriteRestaurants", ignore = true)
    @Mapping(target = "ownerRestaurants", ignore = true)
    @Mapping(target = "customerReviews", ignore = true)
    @Mapping(target = "customerReservations", ignore = true)
    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);
}
