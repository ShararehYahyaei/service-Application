package org.example.serviceapplication.Category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;


public record ServiceCategoryRequest(
        String name) {
}
