package org.example.serviceapplication.workTimer.repository;

import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.workTimer.model.WorkTimer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkTimerRepository extends JpaRepository<WorkTimer, Long> {
    Optional<WorkTimer> findByOffer_Id(Long offerId);


}
