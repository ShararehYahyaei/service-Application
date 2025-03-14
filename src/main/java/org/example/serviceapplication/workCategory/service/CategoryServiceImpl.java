package org.example.serviceapplication.workCategory.service;

import org.example.serviceapplication.subService.service.SubCategoryService;
import org.example.serviceapplication.workCategory.dto.CategoryResponseDto;
import org.example.serviceapplication.workCategory.model.Category;
import org.example.serviceapplication.workCategory.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service

public class CategoryServiceImpl implements CategoryServiceInterface {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

    }

    @Transactional
    @Override
    public Category createNewCategory(Category category) {
        return categoryRepository.save(category);

    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        Optional<Category> categoryFoundById = categoryRepository.findById(id);
        if (categoryFoundById.isPresent()) {
            return convertEntityToResponseDto(categoryFoundById.get());
        }
        throw new RuntimeException("Category not found");

    }


    public void deleteWork(long id) {
        categoryRepository.deleteById(id);
    }

    public List<CategoryResponseDto> getAllWorks() {
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDto> getWorkByName(String name) {
        List<Category> allCategoriesWithContainingName = categoryRepository.findByNameContainingIgnoreCase(name);
        if (allCategoriesWithContainingName.isEmpty()) {
            throw new RuntimeException("Category not found with name " + name);
        }
        return allCategoriesWithContainingName.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());

    }


    private CategoryResponseDto convertEntityToResponseDto(Category category) {
        return new CategoryResponseDto(
                category.getName(),
                category.getSubCategotyList()
        );
    }


}
