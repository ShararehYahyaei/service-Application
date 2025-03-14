package org.example.serviceapplication.workCategory.service;

import org.example.serviceapplication.workCategory.dto.CategoryResponseDto;
import org.example.serviceapplication.workCategory.model.Category;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CategoryServiceInterface {
    Category createNewCategory(Category category);

    CategoryResponseDto getCategoryById(Long id);

    List<CategoryResponseDto> getWorkByName(String name);
}
