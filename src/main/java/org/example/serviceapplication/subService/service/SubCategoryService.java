package org.example.serviceapplication.subService.service;

import org.example.serviceapplication.subService.model.SubCategory;
import org.example.serviceapplication.subService.subCategoryDto.SubCategoryCreateDto;

public interface SubCategoryService {
    SubCategory createSubCategory(SubCategoryCreateDto subCategoryCreateDto);
}
