package org.example.serviceapplication.offer.repository;

import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.offer.model.OfferStatus;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends CrudRepository<Offer, Long> {
        List<Offer> findByCustomerRequestId(Long customer_request_id);
        Optional<Offer> findByUserAndCustomerRequest(User user, CustomerRequest customerRequest);
        List<Offer> findByUserIdAndStatus(Long userId, OfferStatus status);
        Optional<Offer> findFirstByCustomerRequestId(Long customerRequestId);


}
