package org.example.serviceapplication.user.service;

import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.user.dto.*;
import org.example.serviceapplication.user.model.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {


    @Transactional
    UserResponseDto createUser(UserRequest userRequest, MultipartFile profileImage);

    @Transactional(readOnly = true)
    UserResponseDto findById(Long id);

    @Transactional(readOnly = true)
    List<UserResponseDto> getUsersByName(String name);

    @Transactional(readOnly = true)
    List<UserResponseDto> getAllUsers();

    @Transactional(readOnly = true)
    List<UserResponseDto> getAllActiveUsers();

    @Transactional
    void addSubCategory(Long idSpecialist, Long categoryId);

    List<SpecialistWithSubService>getUserWithSubServiceCategory(Long idSpecialist);
    @Transactional
    void removeSubCategoryForSpecialist(Long idSpecialist, Long categoryId);

    void editSubServiceCategory(Long userId, Long subServiceCategoryOld, Long subServiceCategoryNew);

    @Transactional(readOnly = true)
    User getUserById(Long id);

    List<CustomerResponseDto> getAllCustomers();

    List<SpecialistResponseDto> getAllSpecialists();

    void updatePassword(PasswordUserRequest changePasswordRequest);
}
