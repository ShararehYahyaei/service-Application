package org.example.serviceapplication.offer.service;

import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.offer.dto.OfferUpdateDto;
import org.example.serviceapplication.offer.exception.OfferDateIsNotValid;
import org.example.serviceapplication.offer.exception.OfferNotFound;
import org.example.serviceapplication.offer.exception.OfferStatusIsNotCorrect;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.offer.model.OfferStatus;
import org.example.serviceapplication.offer.repository.OfferRepository;
import org.example.serviceapplication.request.exception.RequestNotPresent;
import org.example.serviceapplication.request.exception.RequestStatusIsNotCorrect;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.request.model.RequestStatus;
import org.example.serviceapplication.request.sercvice.CustomerRequestService;
import org.example.serviceapplication.review.service.ReviewService;
import org.example.serviceapplication.user.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferServiceInterface {
    private final OfferRepository offerRepository;
    private final CustomerRequestService request;
    private final ReviewService reviewService;

    public OfferServiceImpl(OfferRepository offerRepository,
                            CustomerRequestService request, ReviewService reviewService) {
        this.offerRepository = offerRepository;
        this.request = request;
        this.reviewService = reviewService;
    }

    @Transactional
    @Override
    public void createOffer(User customer, OfferDto offerDto) {
        Offer offer = convertRequestIntoEntity(customer, offerDto);
        offer.setStatus(OfferStatus.PENDING);
        offer.setOfferDate(LocalDate.now());
        offer.setCreateTime(LocalDateTime.now());
        offerRepository.save(offer);
        offer.getCustomerRequest().setRequestStatus(RequestStatus.AwaitingSelection);

    }

    @Transactional
    @Override
    public void updateOffer(Long offerId, OfferUpdateDto offerUpdateDto) {
        Optional<Offer> byId = offerRepository.findById(offerId);
        if (byId.isEmpty()) {
            throw new OfferNotFound("Offer with customerRequestNumber " + offerId + " not found");
        }
        Offer offer = byId.get();
        offer.setOfferPrice(offerUpdateDto.offerPrice());
        offer.setOfferDate(offerUpdateDto.offerDate());
        offer.setEstimationTime(offerUpdateDto.estimationTime());
        offerRepository.save(offer);
    }


    private Offer convertRequestIntoEntity(User specialist, OfferDto offerDto) {
        CustomerRequest requestOne = request.findRequestById(offerDto.customerRequestId());
        if (requestOne.getRequestStatus() != RequestStatus.InProgress
        ) {
            if (offerDto.offerDate().isAfter(LocalDate.now())) {
                return new Offer(
                        specialist,
                        requestOne,
                        offerDto.offerPrice(),
                        offerDto.offerDate(),
                        offerDto.estimationTime()

                );
            }
            throw new OfferDateIsNotValid("OfferDateIsNotValid");
        } else {
            throw new RequestStatusIsNotCorrect("Request status is not correct");
        }

    }

    private List<OfferDto> toOfferDTOList(List<Offer> offers) {
        return offers.stream()
                .map(offer -> new OfferDto(
                        offer.getId(),
                        offer.getUser().getId(),
                        offer.getOfferPrice(),
                        offer.getOfferDate(),
                        offer.getEstimationTime(),
                        offer.getCustomerRequest().getId(),
                        reviewService.getRateForSpecialist(offer.getUser().getId()) != null ?
                                reviewService.getRateForSpecialist(offer.getUser().getId()) : 0

                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<OfferDto> getAllOffers(Long requestId, Sort sort) {
        List<Offer> offers = offerRepository.findByCustomerRequestId(requestId,sort);
        if (offers.isEmpty()) {
            throw new OfferNotFound("OfferNotFound");
        }
        return toOfferDTOList(offers);
    }

    @Override
    public Offer getOfferById(Long offerId) {
        Optional<Offer> offer = offerRepository.findById(offerId);
        if (offer.isPresent()) {
            return offer.get();
        }
        throw new OfferNotFound("Offer Not found");
    }

    @Override
    public List<OfferDto> getAllMyOfferWithAcceetedStatsus(Long userId) {
        List<Offer> byUserIdAndStatus = offerRepository.findByUserIdAndStatus(userId, OfferStatus.ACCEPTED);
        if (byUserIdAndStatus.isEmpty()) {
            throw new OfferNotFound("OfferNotFound");
        }

        return toOfferDTOList(byUserIdAndStatus);
    }

    @Override
    public User getSpecialist(Long requestId) {
        Optional<Offer> foundOffer = offerRepository.findById(requestId);
        if (foundOffer.isPresent()) {
            return foundOffer.get().getUser();
        }
        throw new OfferNotFound("OfferNotFound");
    }

    @Transactional
    @Override
    public void deleteOffer(Long offerId) {
        Optional<Offer> found = offerRepository.findById(offerId);
        if (found.isEmpty()) {
            throw new OfferNotFound("OfferNotFound");
        }
        if (found.get().getStatus() == OfferStatus.PENDING) {
            offerRepository.deleteById(offerId);
        } else {
            throw new OfferStatusIsNotCorrect("OfferStatusIsNotCorrect");
        }

    }


}

