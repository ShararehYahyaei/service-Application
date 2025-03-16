package org.example.serviceapplication.user.service;

import org.example.serviceapplication.user.dto.UserRequest;
import org.example.serviceapplication.user.dto.UserResponse;
import org.example.serviceapplication.user.dto.UserResponseWithSubCategory;
import org.example.serviceapplication.user.dto.UserResponseWithoutSubCategory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {


    @Transactional
    UserResponse userCreate(UserRequest userRequest);

    @Transactional(readOnly = true)
    UserResponse findById(Long id);

    @Transactional(readOnly = true)
    List<UserResponse> getUsersByName(String name);

    @Transactional(readOnly = true)
    List<UserResponse> getAllUsers();

    @Transactional(readOnly = true)
    List<UserResponse> getAllActiveUsers();

    @Transactional
    UserResponseWithSubCategory addCategoryToSpecialist(Long idSpecialist, Long categoryId);

    @Transactional(readOnly = true)
    UserResponseWithSubCategory getUserWithCategory(Long id);

    @Transactional
    UserResponseWithoutSubCategory deleteCategoryFromSpecialist(Long idSpecialist, Long categoryId);
}
