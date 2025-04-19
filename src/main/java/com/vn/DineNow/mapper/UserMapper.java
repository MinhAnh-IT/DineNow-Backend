package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.UserDTO;
import com.vn.DineNow.payload.response.auth.UserGoogleDTO;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.payload.request.user.UserUpdateDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "userFavoriteRestaurants", ignore = true)
    @Mapping(target = "ownerRestaurants", ignore = true)
    @Mapping(target = "customerReviews", ignore = true)
    @Mapping(target = "customerReservations", ignore = true)
    User toEntity(UserDTO userDTO);

    UserDTO toDTO(User user);

    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "userFavoriteRestaurants", ignore = true)
    @Mapping(target = "ownerRestaurants", ignore = true)
    @Mapping(target = "customerReviews", ignore = true)
    @Mapping(target = "customerReservations", ignore = true)
    User toEntityFromGoogle(UserGoogleDTO userGoogleDTO);

    @Mapping(target = "updatedAt",ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "isVerified", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userFavoriteRestaurants", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "ownerRestaurants", ignore = true)
    @Mapping(target = "customerReviews", ignore = true)
    @Mapping(target = "customerReservations", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDTO(UserUpdateDTO userDTO, @MappingTarget User user);
}
