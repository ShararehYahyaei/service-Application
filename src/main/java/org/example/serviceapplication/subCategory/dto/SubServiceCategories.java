package org.example.serviceapplication.subCategory.dto;

public record SubServiceCategories(
        Long id,
        String name,
        String description,
        double price,
        String categoryName
) {
}
