package org.example.serviceapplication.subCategory.service;

import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryRequest;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryResponse;

import java.util.List;

public interface SubServiceCategoryInterface {
    SubServiceCategoryResponse createSubServiceCategory(SubServiceCategoryRequest subServiceCategoryRequest);

    List<SubServiceCategoryResponse> getSubCategoriesLessThanThisPrice(double price);
}
