package org.example.serviceapplication.user.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import org.example.serviceapplication.user.enumPackage.Role;
import org.springframework.web.multipart.MultipartFile;


public record UserRequest(
        String address,
        String phone,
        String name,
        String lastName,
        String userName,
        String email,
        String password,
        @Enumerated(EnumType.STRING)
        Role role,
        MultipartFile profileImage

) {
}
