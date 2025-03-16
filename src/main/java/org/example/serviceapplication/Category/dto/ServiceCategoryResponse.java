package org.example.serviceapplication.Category.dto;

import org.example.serviceapplication.subCategory.dto.SubServiceCategoryResponseDto;

import java.util.List;

public record ServiceCategoryResponse(String name, List<SubServiceCategoryResponseDto> subCategotyList) {
}
