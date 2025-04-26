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
public class MainCategoryServiceImpl implements MainCategoryService {
    MainCategoryRepository mainCategoryRepository;
    MainCategoryMapper mainCategoryMapper;


    @Override
    public MainCategoryResponse createMainCategory(MainCategoryRequest mainCategoryDTO) throws CustomException {
        if (mainCategoryRepository.existsByName(mainCategoryDTO.getName())){
            throw new CustomException(StatusCode.EXIST_NAME, mainCategoryDTO.getName(), "main category");
        }
        MainCategory mainCategory = mainCategoryMapper.toEntity(mainCategoryDTO);
        mainCategoryRepository.save(mainCategory);
        return mainCategoryMapper.toDto(mainCategory);
    }

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

    @Override
    public List<MainCategoryResponse> getAllMainCategories() throws CustomException {
        List<MainCategory> mainCategories = mainCategoryRepository.findAll();
        return mainCategories.stream()
                .map(mainCategoryMapper::toDto)
                .toList();
    }

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
