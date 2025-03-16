package org.example.serviceapplication.subCategory.dto;

public record SubServiceCategoryResponse(
        String name,
        String description,
        double price,
        String categoryName
) {
}
