package org.example.serviceapplication.user.service.specialistService;

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

    public SpecialistService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SpecialistResponseDto createSpecialist(User user) {
        User specialist = userRepository.save(user);
        return convertEntityToResponseDto(specialist);


    }

    public SpecialistResponseDto convertEntityToResponseDto(User user) {

        if (user.getProfileImage() == null) {
            throw new ProfileImageNull("Profile image is null");
        }

        byte[] profileImageBytes = user.getProfileImage();
        int imageSizeInBytes = profileImageBytes.length;
        double imageSizeInKB = imageSizeInBytes / 1024.0;

        if (imageSizeInKB > 300 * 1024) {
            throw new InvalidImageSize("InvalidImageSize ");
        }
        String profileImageBase64 = convertByteArrayToBase64(profileImageBytes);
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
