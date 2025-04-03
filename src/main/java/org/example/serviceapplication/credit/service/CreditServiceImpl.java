package org.example.serviceapplication.credit.service;

import org.example.serviceapplication.credit.exception.CreditNotFoundException;
import org.example.serviceapplication.credit.model.Credit;
import org.example.serviceapplication.credit.model.CreditDto;
import org.example.serviceapplication.credit.repository.CreditRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditServiceImpl implements CreditService {
    private final CreditRepository creditRepository;

    public CreditServiceImpl(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    @Transactional
    @Override
    public Credit createCredit(CreditDto creditDto) {
        Credit credit = convertCreditDtoCredit(creditDto);
        return creditRepository.save(credit);
    }


    private Credit convertCreditDtoCredit(CreditDto creditDto) {
        Credit credit = new Credit();
        credit.setUserCustomerId(creditDto.userCustomerId());
        credit.setAmount(creditDto.amount());
        return credit;

    }

    @Transactional(readOnly = true)
    @Override
    public Double getCreditAmountByUserId(Long userCustomerId) {
        Credit credit = creditRepository.findByUserCustomerId(userCustomerId);
        if (credit != null) {
            return credit.getAmount();
        } else {
            throw new CreditNotFoundException("Credit not found for user with ID " + userCustomerId);
        }
    }
}
