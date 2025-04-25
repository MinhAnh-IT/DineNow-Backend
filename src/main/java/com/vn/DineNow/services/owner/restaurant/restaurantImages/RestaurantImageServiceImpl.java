package com.vn.DineNow.services.owner.restaurant.restaurantImages;

import com.vn.DineNow.entities.Restaurant;
import com.vn.DineNow.entities.RestaurantImage;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.repositories.RestaurantImageRepository;
import com.vn.DineNow.repositories.RestaurantRepository;
import com.vn.DineNow.services.common.fileService.FileService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class RestaurantImageServiceImpl implements RestaurantImageService {

    final RestaurantRepository restaurantRepository;
    final RestaurantImageRepository restaurantImageRepository;
    final FileService fileService;

    @Override
    @Transactional
    public void saveImages(Long restaurantId, List<MultipartFile> images) throws CustomException {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(StatusCode.RESTAURANT_NOT_FOUND, restaurantId));

        for (MultipartFile image : images) {
            if (image.isEmpty()) continue;

            try {
                String savedFilename = fileService.uploadFile(image);
                RestaurantImage restaurantImage = new RestaurantImage();
                restaurantImage.setImageUrl(savedFilename);
                restaurantImage.setRestaurant(restaurant);
                restaurantImageRepository.save(restaurantImage);
            } catch (IOException e) {
                throw new CustomException(StatusCode.IMAGE_UPLOAD_FAILED, image.getOriginalFilename());
            }
        }
    }

    @Override
    public List<String> getImageUrlsByRestaurantId(Long restaurantId) {
        return restaurantImageRepository.findByRestaurantId(restaurantId).stream()
                .map(img -> fileService.getPublicFileUrl(img.getImageUrl()))
                .toList();
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) throws CustomException {
        RestaurantImage image = restaurantImageRepository.findById(imageId)
                .orElseThrow(() -> new CustomException(StatusCode.IMAGE_NOT_FOUND, imageId));

        try {
            fileService.deleteFile(image.getImageUrl());
        } catch (IOException e) {
            throw new CustomException(StatusCode.IMAGE_DELETE_FAILED, image.getImageUrl());
        }

        restaurantImageRepository.delete(image);
    }

    @Override
    @Transactional
    public void deleteImagesByRestaurantId(Long restaurantId) throws CustomException {
        List<RestaurantImage> images = restaurantImageRepository.findByRestaurantId(restaurantId);
        for (RestaurantImage image : images) {
            try {
                fileService.deleteFile(image.getImageUrl());
            } catch (IOException e) {
                throw new CustomException(StatusCode.IMAGE_DELETE_FAILED, image.getImageUrl());
            }
        }
        restaurantImageRepository.deleteAll(images);
    }

    @Override
    public void validateImageFile(MultipartFile image) throws CustomException {
        if (image.isEmpty()) {
            throw new CustomException(StatusCode.INVALID_IMAGE_TYPE, "File is empty");
        }

        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase()
                : "";

        if (!List.of("png", "jpg", "jpeg", "gif").contains(extension)) {
            throw new CustomException(StatusCode.INVALID_IMAGE_TYPE, extension);
        }

        long maxSizeInBytes = 5 * 1024 * 1024; // 5MB
        if (image.getSize() > maxSizeInBytes) {
            throw new CustomException(StatusCode.INVALID_IMAGE_TYPE, "File exceeds 5MB limit");
        }
    }



}
