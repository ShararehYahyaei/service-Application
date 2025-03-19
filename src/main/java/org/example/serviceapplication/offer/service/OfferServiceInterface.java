package org.example.serviceapplication.offer.service;

import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.offer.dto.OfferUpdateDto;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.user.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OfferServiceInterface {
    void createOffer(User customer, OfferDto offerDto);

    @Transactional
    void updateOffer(User specialist, OfferUpdateDto offerUpdateDto);
    @Transactional(readOnly = true)
    List<OfferDto> getAllOffers( Long requestId);
    Offer getOfferById(Long offerId);

    List<OfferDto> getAllMyOfferWithAcceetedStatsus(Long userId);
}
