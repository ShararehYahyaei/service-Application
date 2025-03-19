package org.example.serviceapplication.offer.service;

import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.offer.dto.OfferUpdateDto;
import org.example.serviceapplication.offer.exception.OfferDateIsNotValid;
import org.example.serviceapplication.offer.exception.OfferNotFound;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.offer.model.OfferStatus;
import org.example.serviceapplication.offer.repository.OfferRepository;
import org.example.serviceapplication.request.exception.RequestNotPresent;
import org.example.serviceapplication.request.exception.RequestStatusIsNotCorrect;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.request.model.RequestStatus;
import org.example.serviceapplication.request.sercvice.CustomerRequestService;
import org.example.serviceapplication.user.model.User;
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

    public OfferServiceImpl(OfferRepository offerRepository,
                            CustomerRequestService request) {
        this.offerRepository = offerRepository;
        this.request = request;
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
    public void updateOffer(User specialist, OfferUpdateDto offerUpdateDto) {
        Offer offer = convertOfferDtoUpdateToEntity(specialist, offerUpdateDto);
        offerRepository.save(offer);
    }


    private Offer convertRequestIntoEntity(User specialist, OfferDto offerDto) {
        CustomerRequest requestOne = request.findRequestById(offerDto.customerRequestId());
        if (requestOne.getRequestStatus() == RequestStatus.AwaitingOffers) {
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

    private static List<OfferDto> toOfferDTOList(List<Offer> offers) {
        return offers.stream()
                .map(offer -> new OfferDto(
                        offer.getId(),
                        offer.getId(),
                        offer.getOfferPrice(),
                        offer.getOfferDate(),
                        offer.getEstimationTime(),
                        offer.getCustomerRequest().getId()

                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<OfferDto> getAllOffers(Long requestId) {
        List<Offer> offers = offerRepository.findByCustomerRequestId(requestId);
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
        Optional<Offer> foundOffer = offerRepository.findFirstByCustomerRequestId(requestId);
        if (foundOffer.isPresent()) {
            return foundOffer.get().getUser();
        }
        throw new OfferNotFound("OfferNotFound");
    }


    private Offer convertOfferDtoUpdateToEntity(User specialist, OfferUpdateDto offerUpdateDto) {
        CustomerRequest requestById = request.findRequestById(offerUpdateDto.CustomerRequestId());
        if (requestById == null) {
            throw new RequestNotPresent("request not found");
        }
        Optional<Offer> offer = offerRepository.findByUserAndCustomerRequest(specialist, requestById);
        if (offer.isEmpty()) {
            throw new OfferNotFound("Offer Not found");
        }

        if (requestById.getRequestStatus() == RequestStatus.AwaitingSelection) {
            if (offerUpdateDto.offerDate().isAfter(LocalDate.now())) {
                return new Offer(
                        offerUpdateDto.offerPrice(),
                        offerUpdateDto.offerDate(),
                        offerUpdateDto.estimationTime()
                );
            }
            throw new OfferDateIsNotValid("OfferDateIsNotValid");
        } else {
            throw new RequestStatusIsNotCorrect("Request status is not correct");
        }

    }

}