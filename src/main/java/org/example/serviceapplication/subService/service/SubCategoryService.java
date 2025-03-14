package org.example.serviceapplication.subService.service;

import org.example.serviceapplication.subService.model.SubCategory;
import org.example.serviceapplication.subService.subCategoryDto.SubCategoryCreateDto;
import org.example.serviceapplication.subService.subCategoryDto.SubCategoryResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SubCategoryService {
    SubCategory createSubCategory(SubCategoryCreateDto subCategoryCreateDto);

    List<SubCategoryResponse> getSubCategoriesLessThanThisPrice(double price);
}
