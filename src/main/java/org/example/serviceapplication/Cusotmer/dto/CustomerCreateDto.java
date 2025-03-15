package org.example.serviceapplication.Cusotmer.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import org.example.serviceapplication.enumPackage.Role;
import org.example.serviceapplication.enumPackage.Status;

public record CustomerCreateDto(

        String address,
        String phone,
        String name,
        String email,
        String password,
        boolean active,
        @Enumerated(EnumType.STRING)
        Role role,
        @Enumerated(EnumType.STRING)
        Status status,
        @Lob
        byte[] profileImage


) {
}
