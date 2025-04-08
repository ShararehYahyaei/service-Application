package org.example.serviceapplication.order.repository;

import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOfferId(Long offerId);
    boolean existsByOffer(Offer offer);
    List<Order> findByCustomerId(Long customerId);
    @Query("SELECT r FROM Order r " +
            "JOIN r.offer ofr " +
            "WHERE ofr.id IN :offers")
    List<Order> findAllByOfferIn(@Param("offers") List<Long> offers);

}
