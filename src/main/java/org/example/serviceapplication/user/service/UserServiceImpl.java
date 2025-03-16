package org.example.serviceapplication.user.service;


import org.example.serviceapplication.user.dto.UserRequest;
import org.example.serviceapplication.user.dto.UserResponse;
import org.example.serviceapplication.user.enumPackage.Status;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.userRepository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserResponse userCreate(UserRequest userRequest) {
        User user = convertRequestIntoEntity(userRequest);
        user.setActive(false);
        user.setStatus(Status.newJoiner);
        user.setCreatedAt(LocalDateTime.now());
        User saveUser = userRepository.save(user);
        return convertEntityToResponseDto(saveUser);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponse findById(Long id) {
        Optional<User> userFound = userRepository.findById(id);
        if (userFound.isPresent()) {
            return convertEntityToResponseDto(userFound.get());
        }
        throw new RuntimeException("User not found");
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponse> getUsersByName(String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        return users.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    @Override
    public List<UserResponse> getAllActiveUsers() {
        List<User> allUsers = userRepository.findByActiveTrue();
        return allUsers.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }


    private User convertRequestIntoEntity(UserRequest userRequest) {

        try {
            // بررسی اینکه تصویر موجود است یا نه
            if (userRequest.profileImage() == null || userRequest.profileImage().isEmpty()) {
                throw new IllegalArgumentException("Profile image is required");
            }

            // بررسی حجم فایل (۳۰۰ کیلوبایت = 300 * 1024 بایت)
            if (userRequest.profileImage().getSize() > 300 * 1024) {
                throw new IllegalArgumentException("Image size exceeds 300KB");
            }

            // تبدیل فایل به آرایه بایت
            byte[] profileImageBytes = userRequest.profileImage().getBytes();
        return new User(
                userRequest.address(),
                userRequest.phone(),
                userRequest.name(),
                userRequest.lastName(),
                userRequest.userName(),
                userRequest.email(),
                userRequest.password(),
                userRequest.role(),
                profileImageBytes
        );
        } catch (IOException e) {
            throw new RuntimeException("Failed to process profile image", e);
        }
    }

    private byte[] convertBase64ToByteArray(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            return null;
        }
        return Base64.getDecoder().decode(base64String);
    }


    private UserResponse convertEntityToResponseDto(User user) {
        return new UserResponse(
                user.getAddress(),
                user.getPhone(),
                user.getName(),
                user.isActive(),
                user.getRole(),
                user.getStatus()

        );
    }


}
