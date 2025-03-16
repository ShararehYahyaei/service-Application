package org.example.serviceapplication.user.service;


import org.example.serviceapplication.Category.exception.NoActiveUsersFound;
import org.example.serviceapplication.Category.exception.NotFoundCategory;
import org.example.serviceapplication.Category.service.ServiceCategoryInterface;
import org.example.serviceapplication.user.dto.*;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.enumPackage.Status;
import org.example.serviceapplication.user.exception.UserHasWrongRole;
import org.example.serviceapplication.user.exception.UserNotFond;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.customerService.CustomerService;
import org.example.serviceapplication.user.service.specialistService.SpecialistService;
import org.example.serviceapplication.user.userRepository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final ServiceCategoryInterface categoryService;
    private final CustomerService customerService;
    private final SpecialistService specialistService;

    public UserServiceImpl(UserRepository userRepository,
                           ServiceCategoryInterface categoryService,
                           CustomerService customerService,
                           SpecialistService specialistService) {

        this.userRepository = userRepository;
        this.categoryService = categoryService;
        this.customerService = customerService;
        this.specialistService = specialistService;
    }

    @Transactional
    @Override
    public UserResponseDto createUser(UserRequest userRequest) {
        UserResponseDto userResponse = null;
        User user = convertRequestIntoEntity(userRequest);
        user.setActive(false);
        user.setStatus(Status.newJoiner);
        user.setCreatedAt(LocalDateTime.now());
        if (user.getRole() == Role.Customer) {
            userResponse = customerService.createCustomer(user);
        } else if (user.getRole() == Role.Specialist) {
            userResponse = specialistService.createSpecialist(user);
        }
        return userResponse;

    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findById(Long id) {
        Optional<User> userFound = userRepository.findById(id);
        if (userFound.isPresent()) {
            return convertUserToResponseDto(userFound.get());
        }
        throw new RuntimeException("User not found");
    }


    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getUsersByName(String name) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        return users.stream()
                .map(this::convertUserToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertUserToResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    @Override
    public List<UserResponseDto> getAllActiveUsers() {
        List<User> allUsers = userRepository.findByActiveTrue();

        if (allUsers.isEmpty()) {
            throw new NoActiveUsersFound("No active users found.");
        }
        return allUsers.stream()
                .map(this::convertUserToResponseDto)
                .collect(Collectors.toList());
    }

    //todo
    @Transactional
    @Override
    public void addSubCategory(Long idSpecialist, Long categoryId) {

        Optional<User> userFound = userRepository.findById(idSpecialist);
        if (userFound.isEmpty()) {
            throw new UserNotFond("User not found");
        }


        if (userFound.get().getRole() == Role.Customer || userFound.get().getRole() == Role.Admin) {
            throw new UserHasWrongRole("User has wrong role");

        }
        specialistService.addSubCategoryToSpecialist(userFound.get(), categoryId);

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
        byte[] image = null;
        try {
            if (userRequest.profileImage() != null) {
                image = userRequest.profileImage().getBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return new User(
                userRequest.address(),
                userRequest.phone(),
                userRequest.name(),
                userRequest.lastName(),
                userRequest.userName(),
                userRequest.email(),
                userRequest.password(),
                userRequest.role(),
                image
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


    private UserResponseDto convertUserToResponseDto(User user) {
        UserResponseDto userResponseDto = null;
        if (user.getRole() == Role.Customer) {
            userResponseDto = customerService.convertEntityToResponseDto(user);
        } else if (user.getRole() == Role.Specialist) {
            userResponseDto = specialistService.convertEntityToResponseDto(user);
        }
        return userResponseDto;
    }

}
