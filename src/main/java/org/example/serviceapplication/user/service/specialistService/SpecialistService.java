package org.example.serviceapplication.user.service.specialistService;

import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.offer.dto.OfferUpdateDto;
import org.example.serviceapplication.request.dto.CustomerRequestResponseDto;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.user.dto.SpecialistResponseDto;
import org.example.serviceapplication.user.dto.SpecialistWithSubService;
import org.example.serviceapplication.user.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SpecialistService {
    @Transactional
    void editSubServiceCategory(User user, Long subServiceCategoryOld, Long subServiceCategoryNew);

    @Transactional
    SpecialistResponseDto createSpecialist(User user);

    @Transactional
    void addSubCategoryToSpecialist(User user, Long subCategoryId);

    @Transactional
    SpecialistResponseDto convertEntityToResponseDto(User user);

    @Transactional
    void removeSubCategory(User userFound, Long subServiceCategoryId);

    @Transactional(readOnly = true)
    List<SpecialistWithSubService> getUserWithSubServiceCategory(User user);

    @Transactional
    void createOffer(User specialist, OfferDto offer);
    User getById(Long id);


    List<CustomerRequestResponseDto> getAllRequests(User specialist);


    void editOffer(Long offerId, OfferUpdateDto offerUpdateDto);

    List<SubServiceCategories> getAllSubServiceCategories();

    List<OfferDto> getAllMyOffersWithAccepetedStatus(Long userId);

    void changeOrderStatus(Long offerId);

    SpecialistResponseDto convertToRes(User user);

    void deleteOffer(Long offerId);
}
