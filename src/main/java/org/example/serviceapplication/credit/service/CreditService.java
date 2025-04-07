package org.example.serviceapplication.credit.service;

import org.example.serviceapplication.credit.model.Credit;
import org.example.serviceapplication.credit.model.CreditDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CreditService {
    @Transactional
    Credit createCredit(CreditDto creditdto);


    CreditDto convertCreditToCreditDto(Credit credit);

    @Transactional(readOnly = true)
   Optional<Credit>  getCreditByUserId(Long userId);
}
