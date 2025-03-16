package org.example.serviceapplication.user.dto;



import java.util.List;

public record UserResponseWithSubCategory(
        String name,
        String lastName,
        String userName,
        boolean active,
        List<String> categories
) {
}
