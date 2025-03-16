package org.example.serviceapplication.user.dto;

public record UserResponseWithoutSubCategory(
        String name,
        String lastName,
        String userName,
        boolean active
) {
}
