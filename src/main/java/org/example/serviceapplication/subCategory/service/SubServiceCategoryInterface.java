package org.example.serviceapplication.subCategory.service;

import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.dto.UpdateSubServiceCategory;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryRequest;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface SubServiceCategoryInterface {
    SubServiceCategoryResponse createSubServiceCategory(SubServiceCategoryRequest subServiceCategoryRequest);

    List<SubServiceCategoryResponse> getSubCategoriesLessThanThisPrice(double price);

    @Transactional(readOnly = true)
    SubServiceCategory getSubServiceCategoryById(long id);

    void editSubServiceCategory(Long id, UpdateSubServiceCategory updateSubServiceCategory);

    void deleteSubServiceCategory(Long id);

    List<SubServiceCategories> getAllSubServiceCatgories();
}
