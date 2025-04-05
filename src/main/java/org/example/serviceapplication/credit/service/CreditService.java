package org.example.serviceapplication.credit.service;

import org.example.serviceapplication.credit.model.Credit;
import org.example.serviceapplication.credit.model.CreditDto;
import org.springframework.transaction.annotation.Transactional;

public interface CreditService {
    @Transactional
    Credit createCredit(CreditDto creditdto);

//    @Transactional(readOnly = true)
//    Double getCreditAmountByUserId(Long userCustomerId);
}
