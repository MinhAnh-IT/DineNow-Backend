package com.vn.DineNow.services.admin.mainCategory;

import com.vn.DineNow.exception.CustomException;
import com.vn.DineNow.payload.request.mainCategory.MainCategoryRequest;
import com.vn.DineNow.payload.request.mainCategory.MainCategoryUpdateRequest;
import com.vn.DineNow.payload.response.mainCategory.MainCategoryResponse;
import java.util.List;

public interface AdminMainCategoryService {
    MainCategoryResponse createMainCategory(MainCategoryRequest mainCategoryDTO) throws CustomException;
    MainCategoryResponse updateMainCategory(Long id, MainCategoryUpdateRequest mainCategoryDTO) throws CustomException;
    List<MainCategoryResponse> getAllMainCategories() throws CustomException;
    boolean deleteMainCategory(Long id) throws CustomException;
}
