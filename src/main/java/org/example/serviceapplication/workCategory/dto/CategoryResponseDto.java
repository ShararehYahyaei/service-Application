package org.example.serviceapplication.workCategory.dto;

import org.example.serviceapplication.subService.model.SubCategory;

import java.util.List;

public record CategoryResponseDto(
        String name,
        List<SubCategory> subCategotyList

) {
}
