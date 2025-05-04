package com.vn.DineNow.mapper;

import com.vn.DineNow.payload.request.user.UserForGenerateToken;
import com.vn.DineNow.payload.request.user.UserCreateRequest;
import com.vn.DineNow.payload.response.auth.UserGoogleDTO;
import com.vn.DineNow.entities.User;
import com.vn.DineNow.payload.request.user.UserUpdateRequest;
import com.vn.DineNow.payload.response.user.UserDetailsResponse;
import com.vn.DineNow.payload.response.user.UserResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userFavoriteRestaurants", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "ownerRestaurants", ignore = true)
    @Mapping(target = "isVerified", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "customerReviews", ignore = true)
    @Mapping(target = "customerReservations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserCreateRequest userDTO);

    UserResponse toResponseDTO(User user);

    UserDetailsResponse toEntityDetails(User user);

    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "providerId", ignore = true)
    @Mapping(target = "userFavoriteRestaurants", ignore = true)
    @Mapping(target = "ownerRestaurants", ignore = true)
    @Mapping(target = "customerReviews", ignore = true)
    @Mapping(target = "customerReservations", ignore = true)
    User toEntityFromGoogle(UserGoogleDTO userGoogleDTO);

    UserForGenerateToken toUserDetailsForGenerateToken(User user);

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
    void updateUserFromDTO(UserUpdateRequest userDTO, @MappingTarget User user);
}
