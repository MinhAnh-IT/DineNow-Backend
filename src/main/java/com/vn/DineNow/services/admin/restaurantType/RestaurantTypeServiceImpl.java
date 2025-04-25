package com.vn.DineNow.services.admin.restaurantType;

import com.vn.DineNow.entities.RestaurantType;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.RestaurantTypeMapper;
import com.vn.DineNow.payload.request.restaurantType.RestaurantTypeRequest;
import com.vn.DineNow.payload.request.restaurantType.RestaurantTypeUpdateRequest;
import com.vn.DineNow.payload.response.RestaurantTypeResponse.RestaurantTypeResponse;
import com.vn.DineNow.repositories.RestaurantTypeRepository;
import com.vn.DineNow.services.common.fileService.FileService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantTypeServiceImpl implements RestaurantTypeService {
    RestaurantTypeRepository restaurantTypeRepository;
    FileService fileService;
    RestaurantTypeMapper restaurantTypeMapper;

    /**
     * Creates a new restaurant type and uploads its image.
     *
     * @param request the request object containing name and image
     * @return the created restaurant type response
     * @throws CustomException if the name already exists or image upload fails
     */
    @Override
    public RestaurantTypeResponse createRestaurantType(RestaurantTypeRequest request) throws CustomException {
        if (request.getName() != null && restaurantTypeRepository.existsByName(request.getName())) {
            throw new CustomException(StatusCode.EXIST_NAME, request.getName(), "restaurant type");
        }
        RestaurantType restaurantType = restaurantTypeMapper.toEntity(request);

        try {
            String imageUrl = fileService.uploadFile(request.getImageUrl());
            restaurantType.setImageUrl(imageUrl);
        } catch (IOException e) {
            throw new CustomException(StatusCode.IMAGE_UPLOAD_FAILED);
        }

        restaurantTypeRepository.save(restaurantType);
        RestaurantTypeResponse response = restaurantTypeMapper.toDTO(restaurantType);
        response.setImageUrl(fileService.getPublicFileUrl(restaurantType.getImageUrl()));
        return response;
    }

    /**
     * Updates an existing restaurant type by ID.
     *
     * @param id      the ID of the restaurant type to update
     * @param request the update request with optional name and image
     * @return the updated restaurant type response
     * @throws CustomException if not found, name already exists, or image upload fails
     */
    @Override
    public RestaurantTypeResponse updateRestaurantType(long id, RestaurantTypeUpdateRequest request) throws CustomException {
        RestaurantType restaurantType = restaurantTypeRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "restaurant type", String.valueOf(id))
        );

        if (request.getName() != null
                && restaurantTypeRepository.existsByName(request.getName())
                && !request.getName().equals(restaurantType.getName())) {
            throw new CustomException(StatusCode.EXIST_NAME, request.getName(), "restaurant type");
        }

        restaurantTypeMapper.UpdateRestaurantTypeFromRequest(restaurantType, request);

        if (request.getImageUrl() != null && fileService.isValidImage(request.getImageUrl())) {
            try {
                String oldImage = restaurantType.getImageUrl();
                String newImageUrl = fileService.uploadFile(request.getImageUrl());
                restaurantType.setImageUrl(newImageUrl);
                if(oldImage != null){
                    fileService.deleteFile(oldImage);
                }
            } catch (IOException e) {
                throw new CustomException(StatusCode.IMAGE_UPLOAD_FAILED);
            }
        }

        restaurantTypeRepository.save(restaurantType);
        RestaurantTypeResponse response = restaurantTypeMapper.toDTO(restaurantType);
        response.setImageUrl(fileService.getPublicFileUrl(restaurantType.getImageUrl()));
        return response;
    }

    /**
     * Retrieves all restaurant types from the system.
     *
     * @return a list of restaurant type responses
     */
    @Override
    public List<RestaurantTypeResponse> getAllRestaurantType() {
        var restaurantTypes = restaurantTypeRepository.findAll();
        return restaurantTypes.stream()
                .map(rt -> {
                    var restaurantType = restaurantTypeMapper.toDTO(rt);
                    restaurantType.setImageUrl(fileService.getPublicFileUrl(rt.getImageUrl()));
                    return restaurantType;
                })
                .toList();
    }

    /**
     * Deletes a restaurant type by ID.
     *
     * @param id the ID of the restaurant type to delete
     * @return true if deletion was successful
     * @throws CustomException if the restaurant type is not found
     */
    @Override
    public boolean deleteRestaurantType(long id) throws CustomException {
        RestaurantType restaurantType = restaurantTypeRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.NOT_FOUND, "restaurant type", String.valueOf(id)));

        try {
            restaurantTypeRepository.delete(restaurantType);
            return true;
        } catch (DataIntegrityViolationException e) {
            // FK constraint violation
            throw new CustomException(StatusCode.RESOURCE_IN_USE, "restaurant type");
        } catch (Exception e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }
}
