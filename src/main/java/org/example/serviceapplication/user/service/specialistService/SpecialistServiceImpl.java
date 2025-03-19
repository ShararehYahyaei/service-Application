package org.example.serviceapplication.user.service.specialistService;


import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.offer.dto.OfferUpdateDto;
import org.example.serviceapplication.offer.service.OfferServiceInterface;
import org.example.serviceapplication.order.service.OrderService;
import org.example.serviceapplication.request.dto.CustomerRequestResponseDto;
import org.example.serviceapplication.request.sercvice.CustomerRequestService;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;

import org.example.serviceapplication.user.dto.SpecialistResponseDto;
import org.example.serviceapplication.user.dto.SpecialistWithSubService;
import org.example.serviceapplication.user.exception.*;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.userRepository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpecialistServiceImpl implements SpecialistService {
    private final OfferServiceInterface offerService;
    private final CustomerRequestService customerRequestService;
    private final OrderService orderService;

    private final UserRepository userRepository;
    private final SubServiceCategoryInterface subServiceCategory;


    public SpecialistServiceImpl(OfferServiceInterface offerService, CustomerRequestService customerRequestService, OrderService orderService,
                                 UserRepository userRepository,
                                 SubServiceCategoryInterface subService) {
        this.offerService = offerService;
        this.customerRequestService = customerRequestService;
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.subServiceCategory = subService;

    }

    @Transactional
    @Override
    public void editSubServiceCategory(User user, Long subServiceCategoryOld, Long subServiceCategoryNew) {
        if (user.getSubServiceCategories() == null || user.getSubServiceCategories().isEmpty()) {
            throw new NotSubServiceCategory("this list is empty");
        }


        SubServiceCategory oldCategory = subServiceCategory.getSubServiceCategoryById(subServiceCategoryOld);
        SubServiceCategory newCategory = subServiceCategory.getSubServiceCategoryById(subServiceCategoryNew);

        if (!user.getSubServiceCategories().contains(oldCategory)) {
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

    @Transactional
    @Override
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

    @Transactional
    @Override
    public void addSubCategoryToSpecialist(User user, Long subCategoryId) {
        SubServiceCategory subService = subServiceCategory.getSubServiceCategoryById(subCategoryId);
        user.getSubServiceCategories().add(subService);
        subService.getUsers().add(user);
        userRepository.save(user);


    }

    @Transactional
    @Override
    public SpecialistResponseDto convertEntityToResponseDto(User user) {
        String profileImageBase64 = convertByteArrayToBase64(user.getProfileImage());
        return new SpecialistResponseDto(
                user.getId(),
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

    @Transactional
    @Override
    public void removeSubCategory(User userFound, Long subServiceCategoryId) {
        SubServiceCategory subService = subServiceCategory.getSubServiceCategoryById(subServiceCategoryId);
        userFound.getSubServiceCategories().remove(subService);
        subService.getUsers().remove(userFound);
        subService.getUsers().remove(userFound);
    }


    @Transactional(readOnly = true)
    @Override
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

    @Transactional
    @Override
    public void createOffer(User specialist, OfferDto offer) {
        offerService.createOffer(specialist, offer);
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserNotFond("User not found");

    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerRequestResponseDto> getAllRequests(User specialist) {
        List<SubServiceCategory> subServiceCategories = specialist.getSubServiceCategories();
        return customerRequestService.
                getAllRequestForSpecialist(subServiceCategories);

    }

    @Transactional
    @Override
    public void editOffer(Long offerId, OfferUpdateDto offerDto) {
        offerService.updateOffer(offerId, offerDto);
    }


    @Override
    public List<SubServiceCategories> getAllSubServiceCategories() {
        return subServiceCategory.getAllSubServiceCatgories();
    }

    @Override
    public List<OfferDto> getAllMyOffersWithAccepetedStatus(Long userId) {
        return offerService.getAllMyOfferWithAcceetedStatsus(userId);
    }
@Transactional
    @Override
    public void changeOrderStatus(Long offerId) {
        orderService.changeOrderStatus(offerId);

    }

    public List<SpecialistResponseDto> convertEntitiesToResponseDtos(List<User> users) {
        return users.stream()
                .map(user -> new SpecialistResponseDto(
                        user.getId(),
                        user.getAddress(),
                        user.getPhone(),
                        user.getName(),
                        user.isActive(),
                        user.getRole(),
                        user.getStatus(),
                        (user.getProfileImage() != null && user.getProfileImage().length > 0)
                                ? Base64.getEncoder().encodeToString(user.getProfileImage())
                                : null
                ))
                .collect(Collectors.toList());
    }
    @Override
    public SpecialistResponseDto convertToRes(User user) {
        return new SpecialistResponseDto(
                user.getId(),
                user.getAddress(),
                user.getPhone(),
                user.getName(),
                user.isActive(),
                user.getRole(),
                user.getStatus(),
                (user.getProfileImage() != null && user.getProfileImage().length > 0)
                        ? Base64.getEncoder().encodeToString(user.getProfileImage())
                        : null
        );
    }

    @Transactional
    @Override
    public void deleteOffer(Long offerId) {

        offerService.deleteOffer(offerId);
    }


}
