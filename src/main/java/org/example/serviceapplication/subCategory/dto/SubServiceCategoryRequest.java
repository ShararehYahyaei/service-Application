package org.example.serviceapplication.subCategory.dto;

import jakarta.validation.constraints.NotNull;

public record SubServiceCategoryRequest(
        @NotNull
        Long id,
        String name,
        String description,
        double price,
        Long categoryId


) {
}
