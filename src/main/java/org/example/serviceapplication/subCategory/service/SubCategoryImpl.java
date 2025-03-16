package org.example.serviceapplication.subCategory.service;

import org.example.serviceapplication.subCategory.model.SubCategory;
import org.example.serviceapplication.subCategory.repsitory.SubCategoryRepository;
import org.example.serviceapplication.subCategory.dto.SubCategoryRequest;
import org.example.serviceapplication.subCategory.dto.SubCategoryResponse;
import org.example.serviceapplication.Category.model.Category;
import org.example.serviceapplication.Category.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SubCategoryImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final CategoryService categoryService;

    public SubCategoryImpl(SubCategoryRepository subCategoryRepository,
                           CategoryService categoryService) {
        this.subCategoryRepository = subCategoryRepository;
        this.categoryService = categoryService;
    }


    @Transactional
    @Override
    public SubCategory createSubCategory(SubCategoryRequest subCategoryRequest) {
        SubCategory subCategory = convertSubCategoryDtoToSubCategory(subCategoryRequest);
        categoryService.updateCategory(subCategory.getCategory());
        return subCategoryRepository.save(subCategory);

    }


    @Transactional(readOnly = true)
    @Override
    public List<SubCategoryResponse> getSubCategoriesLessThanThisPrice(double price) {
        List<SubCategory> subCategories = subCategoryRepository.findByPriceLessThan(price);
        return subCategories.stream()
                .map(this::convertSubCategoryToResponse)
                .collect(Collectors.toList());
    }


    private SubCategory convertSubCategoryDtoToSubCategory
            (SubCategoryRequest subCategoryRequest) {
        Category category = categoryService.getCatgeoryById(subCategoryRequest.categoryId());
        return new SubCategory(
                subCategoryRequest.name(),
                subCategoryRequest.description(),
                subCategoryRequest.price(),
                category
        );
    }


    private SubCategoryResponse convertSubCategoryToResponse
            (SubCategory subCategory) {
        return new SubCategoryResponse(
                subCategory.getName(),
                subCategory.getDescription(),
                subCategory.getPrice(),
                subCategory.getCategory().getName()

        );
    }


}
