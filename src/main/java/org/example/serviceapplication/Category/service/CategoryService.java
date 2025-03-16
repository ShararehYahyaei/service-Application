package org.example.serviceapplication.Category.service;

import org.example.serviceapplication.Category.dto.CategoryRequest;
import org.example.serviceapplication.Category.dto.CategoryResponse;
import org.example.serviceapplication.Category.model.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryService {


    @Transactional
    Category createNewCategory(CategoryRequest categoryRequest);

    CategoryResponse getCategoryByIdResponse(Long id);

    @Transactional(readOnly = true)
    Category getCatgeoryById(Long id);

    @Transactional
    Category updateCategory(Category category);

    @Transactional
    void deleteWork(long id);

    @Transactional(readOnly = true)
    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getAllCategoriesByName(String name);
}
