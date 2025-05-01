package com.vn.DineNow.services.admin.mainCategory;

import com.vn.DineNow.entities.MainCategory;
import com.vn.DineNow.enums.StatusCode;
import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.mapper.MainCategoryMapper;
import com.vn.DineNow.payload.request.mainCategory.MainCategoryRequest;
import com.vn.DineNow.payload.request.mainCategory.MainCategoryUpdateRequest;
import com.vn.DineNow.payload.response.mainCategory.MainCategoryResponse;
import com.vn.DineNow.repositories.MainCategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminMainCategoryServiceImpl implements AdminMainCategoryService {
    MainCategoryRepository mainCategoryRepository;
    MainCategoryMapper mainCategoryMapper;


    /**
     * Creates a new main category.
     *
     * @param mainCategoryDTO the main category request data
     * @return the created main category response
     * @throws CustomException if the name already exists
     */
    @Override
    public MainCategoryResponse createMainCategory(MainCategoryRequest mainCategoryDTO) throws CustomException {
        if (mainCategoryRepository.existsByName(mainCategoryDTO.getName())){
            throw new CustomException(StatusCode.EXIST_NAME, mainCategoryDTO.getName(), "main category");
        }
        MainCategory mainCategory = mainCategoryMapper.toEntity(mainCategoryDTO);
        mainCategoryRepository.save(mainCategory);
        return mainCategoryMapper.toDto(mainCategory);
    }

    /**
     * Updates an existing main category by ID.
     *
     * @param id                the ID of the main category to update
     * @param mainCategoryDTO   the main category update request data
     * @return the updated main category response
     * @throws CustomException if the main category is not found or name already exists
     */
    @Override
    public MainCategoryResponse updateMainCategory(Long id, MainCategoryUpdateRequest mainCategoryDTO) throws CustomException {
        MainCategory mainCategory = mainCategoryRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "main category", String.valueOf(id))
        );
        if(mainCategoryDTO.getName() != null &&
                !mainCategory.getName().equals(mainCategoryDTO.getName()) &&
                mainCategoryRepository.existsByName(mainCategoryDTO.getName())){
            throw new CustomException(StatusCode.EXIST_NAME, mainCategoryDTO.getName(), "main category");
        }

        mainCategoryMapper.updateEntityFromDto(mainCategoryDTO, mainCategory);
        mainCategoryRepository.save(mainCategory);
        return mainCategoryMapper.toDto(mainCategory);
    }

    /**
     * Retrieves all main categories.
     *
     * @return a list of main category responses
     * @throws CustomException if any error occurs
     */
    @Override
    public List<MainCategoryResponse> getAllMainCategories() throws CustomException {
        List<MainCategory> mainCategories = mainCategoryRepository.findAll();
        return mainCategories.stream()
                .map(mainCategoryMapper::toDto)
                .toList();
    }

    /**
     * Deletes a main category by ID.
     *
     * @param id the ID of the main category to delete
     * @return true if the deletion was successful, false otherwise
     * @throws CustomException if the main category is not found or in use
     */
    @Override
    public boolean deleteMainCategory(Long id) throws CustomException {
        MainCategory mainCategory = mainCategoryRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND, "main category", String.valueOf(id))
        );

        try {
            mainCategoryRepository.delete(mainCategory);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(StatusCode.RESOURCE_IN_USE, "main category");
        } catch (Exception e) {
            throw new CustomException(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }
}
