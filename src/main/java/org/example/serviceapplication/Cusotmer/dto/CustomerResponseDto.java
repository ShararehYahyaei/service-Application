package org.example.serviceapplication.Cusotmer.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.example.serviceapplication.enumPackage.Role;
import org.example.serviceapplication.enumPackage.Status;

public record CustomerResponseDto(

        String address,
        String phone,
        String name,
        boolean active,
        @Enumerated(EnumType.STRING)
        Role role,
        @Enumerated(EnumType.STRING)
        Status status

) {
}
