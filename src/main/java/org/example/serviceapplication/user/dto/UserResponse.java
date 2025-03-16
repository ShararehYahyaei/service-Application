package org.example.serviceapplication.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.enumPackage.Status;


public record UserResponse(

        String address,
        String phone,
        String name,
        boolean active,
        @Enumerated(EnumType.STRING)
        Role role,
        @Enumerated(EnumType.STRING)
        Status status,
        String profileImage


) {
}
