package org.example.serviceapplication.user.service.specialistService;


import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.dto.SpecialistResponseDto;
import org.example.serviceapplication.user.dto.SpecialistWithSubService;
import org.example.serviceapplication.user.exception.*;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.UserService;
import org.example.serviceapplication.user.userRepository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpecialistService {

    private final UserRepository userRepository;
    private final SubServiceCategoryInterface subServiceCategory;


    public SpecialistService(UserRepository userRepository, SubServiceCategoryInterface subService) {
        this.userRepository = userRepository;
        this.subServiceCategory = subService;

    }

    public void editSubServiceCategory(User user, Long subServiceCategoryOld, Long subServiceCategoryNew) {
        if (user.getSubServiceCategories() == null||user.getSubServiceCategories().isEmpty()) {
            throw new NotSubServiceCategory("this list is empty");
        }


        SubServiceCategory oldCategory = subServiceCategory.getSubServiceCategoryById(subServiceCategoryOld);
        SubServiceCategory newCategory = subServiceCategory.getSubServiceCategoryById(subServiceCategoryNew);

        if (! user.getSubServiceCategories().contains(oldCategory)) {
            throw new NotSubServiceCategory("User must have  this SubServiceCategory");
        }


        if (oldCategory != null) {
            user.getSubServiceCategories().remove(oldCategory);
            oldCategory.getUsers().remove(user);
        }

        if (newCategory != null) {
            if (user.getSubServiceCategories().contains(newCategory)) {
                throw new UserAlreadyHasThisSubService("User already has this SubServiceCategory");
            }
            user.getSubServiceCategories().add(newCategory);
            newCategory.getUsers().add(user);
        }

        userRepository.save(user);
    }


    public SpecialistResponseDto createSpecialist(User user) {
        if (user.getProfileImage() == null) {
            throw new ProfileImageNull("Profile image is null");
        }

        byte[] profileImageBytes = user.getProfileImage();
        int imageSizeInBytes = profileImageBytes.length;
        double imageSizeInKB = imageSizeInBytes / 1024.0;

        if (imageSizeInKB > 300 * 1024) {
            throw new InvalidImageSize("InvalidImageSize ");
        }
        User specialist = userRepository.save(user);
        return convertEntityToResponseDto(specialist);

    }

    public void addSubCategoryToSpecialist(User user, Long subCategoryId) {
        SubServiceCategory subService = subServiceCategory.getSubServiceCategoryById(subCategoryId);
        user.getSubServiceCategories().add(subService);
        subService.getUsers().add(user);
        userRepository.save(user);


    }


    public SpecialistResponseDto convertEntityToResponseDto(User user) {
        String profileImageBase64 = convertByteArrayToBase64(user.getProfileImage());
        return new SpecialistResponseDto(
                user.getAddress(),
                user.getPhone(),
                user.getName(),
                user.isActive(),
                user.getRole(),
                user.getStatus(),
                profileImageBase64

        );
    }

    private String convertByteArrayToBase64(byte[] bytes) {
        return bytes != null ? Base64.getEncoder().encodeToString(bytes) : null;
    }

    public void removeSubCategory(User userFound, Long subServiceCategoryId) {
        SubServiceCategory subService = subServiceCategory.getSubServiceCategoryById(subServiceCategoryId);
        userFound.getSubServiceCategories().remove(subService);
        subService.getUsers().remove(userFound);
        subService.getUsers().remove(userFound);
    }

    public List<SpecialistWithSubService> getUserWithSubServiceCategory(User user) {
        List<SubServiceCategory> subServiceCategories = user.getSubServiceCategories();
        return getAllSubServiceCategory(subServiceCategories);
    }


    private List<SpecialistWithSubService> getAllSubServiceCategory(List<SubServiceCategory> subServices) {
        return subServices.stream()
                .map(subService -> new SpecialistWithSubService(
                        subService.getName(),
                        subService.getDescription(),
                        subService.getPrice()
                ))
                .collect(Collectors.toList());
    }

}
