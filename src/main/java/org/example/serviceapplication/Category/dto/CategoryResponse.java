package org.example.serviceapplication.Category.dto;

import org.example.serviceapplication.subCategory.model.SubCategory;

import java.util.List;

public record CategoryResponse(
        String name,
        List<SubCategoryResponseDto> subCategotyList

) {
}
