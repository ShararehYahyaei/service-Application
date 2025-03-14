package org.example.serviceapplication.subService.service;

import org.example.serviceapplication.subService.model.SubCategory;
import org.example.serviceapplication.subService.repsitory.SubCategoryRepository;
import org.example.serviceapplication.subService.subCategoryDto.SubCategoryCreateDto;
import org.example.serviceapplication.subService.subCategoryDto.SubCategoryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SubCategoryImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryImpl(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }


    @Transactional
    @Override
    public SubCategory createSubCategory(SubCategoryCreateDto subCategoryCreateDto) {
        SubCategory subCategory = convertSubCategoryDtoToSubCategory(subCategoryCreateDto);
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
            (SubCategoryCreateDto subCategoryCreateDto) {
        return new SubCategory(
                subCategoryCreateDto.name(),
                subCategoryCreateDto.description(),
                subCategoryCreateDto.price()

        );
    }

    private SubCategoryResponse convertSubCategoryToResponse
            (SubCategory SubCategory) {
        return new SubCategoryResponse(
                SubCategory.getName(),
                SubCategory.getDescription(),
                SubCategory.getPrice()

        );
    }


}
