package org.example.serviceapplication.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.enumPackage.Status;

public record CustomerResponseDto(

        String address,
        String phone,
        String name,
        boolean active,
        @Enumerated(EnumType.STRING)
        Role role,
        @Enumerated(EnumType.STRING)
        Status status


) implements UserResponseDto {
}
