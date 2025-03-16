package org.example.serviceapplication.user.service;

import org.example.serviceapplication.user.dto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {


    @Transactional
    UserResponseDto createUser(UserRequest userRequest);

    @Transactional(readOnly = true)
    UserResponseDto findById(Long id);

    @Transactional(readOnly = true)
    List<UserResponseDto> getUsersByName(String name);

    @Transactional(readOnly = true)
    List<UserResponseDto> getAllUsers();

    @Transactional(readOnly = true)
    List<UserResponseDto> getAllActiveUsers();

    @Transactional
    UserResponseWithSubCategory addCategoryToSpecialist(Long idSpecialist, Long categoryId);

    @Transactional(readOnly = true)
    UserResponseWithSubCategory getUserWithCategory(Long id);

    @Transactional
    UserResponseWithoutSubCategory deleteCategoryFromSpecialist(Long idSpecialist, Long categoryId);
}
