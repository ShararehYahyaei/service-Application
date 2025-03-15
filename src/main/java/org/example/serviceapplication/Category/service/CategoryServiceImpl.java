package org.example.serviceapplication.Category.service;

import org.example.serviceapplication.Category.dto.CategoryRequest;
import org.example.serviceapplication.Category.dto.CategoryResponse;
import org.example.serviceapplication.Category.dto.SubCategoryResponseDto;
import org.example.serviceapplication.Category.exception.DuplicateCategoryName;
import org.example.serviceapplication.Category.model.Category;
import org.example.serviceapplication.Category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service

public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

    }

    @Transactional
    @Override
    public Category createNewCategory(CategoryRequest categoryRequest) {
        Category category = convertDtoToEntity(categoryRequest);
        if (categoryRepository.existsByName(category.getName())) {
            throw new DuplicateCategoryName("این نام قبلاً در سیستم وجود دارد.");
        }
        return categoryRepository.save(category);

    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponse getCategoryById(Long id) {
        Optional<Category> categoryFoundById = categoryRepository.findById(id);
        if (categoryFoundById.isPresent()) {
            return convertEntityToResponseDto(categoryFoundById.get());
        }
        throw new RuntimeException("Category not found");

    }

    @Transactional(readOnly = true)
    @Override
    public Category getCategoryByIdForCreateSubCategory(Long id) {
        Optional<Category> categoryFoundById = categoryRepository.findById(id);
        if (categoryFoundById.isPresent()) {
            return categoryFoundById.get();
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    @Transactional
    @Override
    public Category updateCategory(Category category) {
        Optional<Category> categoryFound = categoryRepository.findById(category.getId());
        if (categoryFound.isEmpty()) {
            throw new RuntimeException("Category not found");
        }
        return categoryRepository.save(category);

    }

    @Transactional
    @Override
    public void deleteWork(long id) {
        Optional<Category> categoryFound = categoryRepository.findById(id);
        if (categoryFound.isEmpty()) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponse> getAllCategoriesByName(String name) {
        List<Category> allCategoriesWithContainingName = categoryRepository.findByNameContainingIgnoreCase(name);
        if (allCategoriesWithContainingName.isEmpty()) {
            throw new RuntimeException("Category not found with name " + name);
        }
        return allCategoriesWithContainingName.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());

    }

    private Category convertDtoToEntity(CategoryRequest categoryRequest) {
        return new Category(
                categoryRequest.name()
        );
    }


    private CategoryResponse convertEntityToResponseDto(Category category) {


        return new CategoryResponse(
                category.getName(),
                category.getSubCategoryList().stream()
                        .map(subCategory -> new SubCategoryResponseDto(
                                subCategory.getName(),
                                subCategory.getDescription(),
                                subCategory.getPrice()
                        ))
                        .collect(Collectors.toList())

        );
    }


}
