package org.example.serviceapplication.user.service.specialistService;

import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.dto.SpecialistResponseDto;
import org.example.serviceapplication.user.exception.InvalidImageSize;
import org.example.serviceapplication.user.exception.ProfileImageNull;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.userRepository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class SpecialistService {

    private final UserRepository userRepository;
    private final SubServiceCategoryInterface subServiceCategory;

    public SpecialistService(UserRepository userRepository, SubServiceCategoryInterface subService) {
        this.userRepository = userRepository;
        this.subServiceCategory = subService;
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

}
