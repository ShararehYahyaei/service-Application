package org.example.serviceapplication.subCategory.dto;

public record SubServiceCategoryRequest(
        String name,
        String description,
        double price,
        Long categoryId


) {
}
