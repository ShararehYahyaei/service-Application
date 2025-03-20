package org.example.serviceapplication.user.dto;

public record PasswordUserRequest(Long userId, String password,String confirmPassword) {
}
