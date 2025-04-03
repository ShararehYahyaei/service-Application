package org.example.serviceapplication.offer.service;

import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.offer.dto.OfferUpdateDto;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.user.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OfferServiceInterface {
    void createOffer(User customer, OfferDto offerDto);

    @Transactional
    void updateOffer(Long offerId, OfferUpdateDto offerUpdateDto);

    @Transactional(readOnly = true)
    List<OfferDto> getAllOffersNotSorted(Long requestId, Sort sort);

    Offer getOfferById(Long offerId);

    List<OfferDto> getAllOffersWithAccepetedStatus(Long userId);

    User getSpecialist(Long requestId);

    void deleteOffer(Long offerId);

    List<OfferDto> getAllOffersNotSorted( CustomerRequest customerRequest);

    List<OfferDto> getAllOffersSortedByPrice(CustomerRequest request);

    @Transactional
    List<OfferDto>getAllOffersBySpecialistId(Long userId);

    //  List<OfferDto> getAllOffersSortedByRate(CustomerRequest request);
}
