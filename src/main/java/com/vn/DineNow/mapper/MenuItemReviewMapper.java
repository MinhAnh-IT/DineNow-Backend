package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.MenuItemReviewDTO;
import com.vn.DineNow.entities.MenuItemReview;
import com.vn.DineNow.payload.request.review.MenuItemReviewRequestDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MenuItemReviewMapper {

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "menuItem", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    MenuItemReview toEntity(MenuItemReviewRequestDTO dto);


    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullName", target = "username")
    MenuItemReviewDTO toDTO(MenuItemReview review);
}
