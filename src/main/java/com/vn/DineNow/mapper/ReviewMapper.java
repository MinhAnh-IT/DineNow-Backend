package com.vn.DineNow.mapper;

import com.vn.DineNow.entities.Review;
import com.vn.DineNow.payload.request.review.ReviewRequestDTO;
import com.vn.DineNow.payload.response.review.ReviewResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "restaurant.id", ignore = true)
    @Mapping(target = "customer.id", ignore = true)
    Review toEntity(ReviewRequestDTO reviewDTO);


    @Mapping(target = "reviewerName", source = "customer.fullName")
    @Mapping(target = "reviewDate", source = "createdAt")
    ReviewResponse toDTO(Review review);
}
