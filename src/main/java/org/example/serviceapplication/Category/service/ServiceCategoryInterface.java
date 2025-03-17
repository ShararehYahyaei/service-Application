package org.example.serviceapplication.Category.service;

import org.example.serviceapplication.Category.dto.ServiceCategoryRequest;
import org.example.serviceapplication.Category.dto.ServiceCategoryResponse;
import org.example.serviceapplication.Category.model.ServiceCategory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ServiceCategoryInterface {


    @Transactional
    ServiceCategory createNewCategory(ServiceCategoryRequest serviceCategoryRequest);

    ServiceCategoryResponse getCategoryByIdResponse(Long id);

    @Transactional(readOnly = true)
    ServiceCategory getCategoryById(Long id);

    @Transactional
    ServiceCategory updateCategory(ServiceCategory category);

    @Transactional
    void deleteWork(long id);

    @Transactional(readOnly = true)
    List<ServiceCategoryResponse> getAllCategories();

    List<ServiceCategoryResponse> getAllCategoriesByName(String name);

    void editServiceCategory(Long categoryId, String name);

    void deleteServiceCategory(Long categoryId);
}
