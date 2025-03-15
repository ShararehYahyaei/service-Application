package org.example.serviceapplication.subCategory.service;

import org.example.serviceapplication.subCategory.model.SubCategory;
import org.example.serviceapplication.subCategory.dto.SubCategoryRequest;
import org.example.serviceapplication.subCategory.dto.SubCategoryResponse;

import java.util.List;

public interface SubCategoryService {
    SubCategory createSubCategory(SubCategoryRequest subCategoryRequest);

    List<SubCategoryResponse> getSubCategoriesLessThanThisPrice(double price);
}
