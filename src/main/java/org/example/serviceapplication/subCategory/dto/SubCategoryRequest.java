package org.example.serviceapplication.subCategory.dto;

public record SubCategoryRequest(
        String name,
        String description,
        double price,
        Long categoryId


) {
}
