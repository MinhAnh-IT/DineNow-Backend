package com.vn.DineNow.services.owner.restaurant.restaurantImages;

import com.vn.DineNow.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantImageService {

    void saveImages(Long restaurantId, List<MultipartFile> images) throws CustomException;

    List<String> getImageUrlsByRestaurantId(Long restaurantId) throws CustomException;

    void deleteImage(Long imageId) throws CustomException;

    void deleteImagesByRestaurantId(Long restaurantId) throws CustomException;
    void validateImageFile(MultipartFile image) throws CustomException;
}
