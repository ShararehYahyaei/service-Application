package org.example.serviceapplication.credit.repository;

import org.example.serviceapplication.credit.model.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    Credit findByUserCustomerId(Long userCustomerId);
}
