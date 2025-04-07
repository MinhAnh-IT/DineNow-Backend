package com.vn.DineNow.mapper;

import com.vn.DineNow.dtos.ReviewDTO;
import com.vn.DineNow.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "restaurant", target = "restaurant.id")
    @Mapping(source = "customer", target = "customer.id")
    Review toEntity(ReviewDTO reviewDTO);

    @Mapping(source = "restaurant.id", target = "restaurant")
    @Mapping(source = "customer.id", target = "customer")
    ReviewDTO toDTO(Review review);
}
