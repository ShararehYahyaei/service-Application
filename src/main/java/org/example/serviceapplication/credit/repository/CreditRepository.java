package org.example.serviceapplication.credit.repository;

import org.example.serviceapplication.credit.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    Optional<Credit> findByUserId(Long userId);
}
