package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.MenuItemReview;
import com.vn.DineNow.payload.request.review.ReviewRequestDTO;
import com.vn.DineNow.payload.response.review.ReviewResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MenuItemReviewMapper {
    @Mapping(target = "orderItem", ignore = true)
    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "menuItem.id", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    MenuItemReview toEntity(ReviewRequestDTO reviewDto);

    @Mapping(target = "reviewerName", source = "user.fullName")
    @Mapping(target = "reviewDate", source = "createdAt")
    ReviewResponse toDTO(MenuItemReview review);
}
