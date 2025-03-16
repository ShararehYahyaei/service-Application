package org.example.serviceapplication.user.service;


import org.example.serviceapplication.Category.exception.NoActiveUsersFound;
import org.example.serviceapplication.Category.exception.NotFoundCategory;
import org.example.serviceapplication.Category.service.ServiceCategoryInterface;
import org.example.serviceapplication.user.dto.UserRequest;
import org.example.serviceapplication.user.dto.UserResponse;
import org.example.serviceapplication.user.dto.UserResponseWithSubCategory;
import org.example.serviceapplication.user.dto.UserResponseWithoutSubCategory;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.enumPackage.Status;
import org.example.serviceapplication.user.exception.UserHasNoAnyService;
import org.example.serviceapplication.user.exception.UserHasWrongRole;
import org.example.serviceapplication.user.exception.UserNotFond;
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
    private final ServiceCategoryInterface categoryService;

    public UserServiceImpl(UserRepository userRepository, ServiceCategoryInterface categoryService) {

        this.userRepository = userRepository;
        this.categoryService = categoryService;
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

        if (allUsers.isEmpty()) {
            throw new NoActiveUsersFound("No active users found.");
        }
        return allUsers.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public UserResponseWithSubCategory addCategoryToSpecialist(Long idSpecialist, Long categoryId) {

        Optional<User> userFound = userRepository.findById(idSpecialist);

        if (userFound.isPresent()) {
            User user = userFound.get();
            if (user.getRole() == Role.Specialist) {
                org.example.serviceapplication.Category.model.ServiceCategory categoryFound = categoryService.getCategoryById(categoryId);
//                user.getCategories().add(categoryFound);
//                User userSave = userRepository.save(user);
//                categoryFound.getUsers().add(userSave);
//                categoryService.updateCategory(categoryFound);
                return null;//convertEntityToResponseWithCategory(userSave);
            } else {
                throw new UserHasWrongRole("User is not a Specialist");
            }
        } else {
            throw new UserNotFond("User not found");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseWithSubCategory getUserWithCategory(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFond("User not found"));

//        if (user.getRole() != Role.Specialist || user.getCategories() == null || user.getCategories().isEmpty()) {
//            throw new UserNotFond("User has no specialty or services available");
//        }

        return convertEntityToResponseWithCategory(user);
    }

    @Transactional
    @Override
    public UserResponseWithoutSubCategory deleteCategoryFromSpecialist(Long idSpecialist, Long categoryId) {
        User user = userRepository.findById(idSpecialist)
                .orElseThrow(() -> new UserNotFond("Specialist not found"));

        if (user.getRole() != Role.Specialist) {
            throw new UserHasWrongRole("User is not a Specialist");
        }

        org.example.serviceapplication.Category.model.ServiceCategory category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            throw new NotFoundCategory("Category not found");
        }
//        if (user.getCategories().contains(category)) {
//            user.getCategories().remove(category);
//        } else {
//            throw new UserHasNoAnyService("Specialist does not have this category");
//        }

        User saveUser = userRepository.save(user);
        categoryService.updateCategory(category);
        return convertEntityToWithoutCategory(saveUser);
    }


    private User convertRequestIntoEntity(UserRequest userRequest) {

        try {

            if (userRequest.profileImage() == null || userRequest.profileImage().isEmpty()) {
                throw new IllegalArgumentException("Profile image is required");
            }


            if (userRequest.profileImage().getSize() > 300 * 1024) {
                throw new IllegalArgumentException("Image size exceeds 300KB");
            }

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


    private UserResponse convertEntityToResponseDto(User user) {
        String profileImageBase64 = convertByteArrayToBase64(user.getProfileImage());
        return new UserResponse(
                user.getAddress(),
                user.getPhone(),
                user.getName(),
                user.isActive(),
                user.getRole(),
                user.getStatus(),
                profileImageBase64


        );
    }

    private UserResponseWithSubCategory convertEntityToResponseWithCategory(User user) {
        return null;
//        List<String> categoryNames = user.getCategories().stream()
//                .map(org.example.serviceapplication.Category.model.ServiceCategory::getName)
//                .toList();
//        return new UserResponseWithSubCategory(
//                user.getName(),
//                user.getLastName(),
//                user.getUserName(),
//                user.isActive(),
//                categoryNames
//
//        );
    }

    private UserResponseWithoutSubCategory convertEntityToWithoutCategory(User user) {
        return new UserResponseWithoutSubCategory(
                user.getName(),
                user.getLastName(),
                user.getUserName(),
                user.isActive()

        );
    }


    private String convertByteArrayToBase64(byte[] bytes) {
        return bytes != null ? Base64.getEncoder().encodeToString(bytes) : null;
    }


}
