package org.example.serviceapplication.subCategory.dto;

public record SubCategoryResponse(
        String name,
        String description,
        double price,
        String categoryName
) {
}
