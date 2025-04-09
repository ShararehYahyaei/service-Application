package org.example.serviceapplication.credit.service;

import org.example.serviceapplication.credit.exception.CreditNotFoundException;
import org.example.serviceapplication.credit.model.Credit;
import org.example.serviceapplication.credit.model.CreditDto;
import org.example.serviceapplication.credit.model.CreditStatus;
import org.example.serviceapplication.credit.repository.CreditRepository;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CreditServiceImpl implements CreditService {
    private final CreditRepository creditRepository;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(CreditServiceImpl.class);

    public CreditServiceImpl(CreditRepository creditRepository, UserService userService) {
        this.creditRepository = creditRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public Credit createCredit(CreditDto creditDto) {
        Credit credit = convertCreditDtoCredit(creditDto);
        credit.setStatus(CreditStatus.Active);
        return creditRepository.save(credit);
    }

    private Credit convertCreditDtoCredit(CreditDto creditDto) {
        User user = userService.getUserById(creditDto.userId().longValue());
        return new Credit(
                user,
                creditDto.balance().doubleValue()
        );

    }
    @Override
    public CreditDto convertCreditToCreditDto(Credit credit) {
        return new CreditDto(
                credit.getId(),
                credit.getBalance(),
                credit.getStatus()
        );
    }



    @Transactional(readOnly = true)
    @Override
    public   Optional<Credit>  getCreditByUserId(Long userId) {
        Optional<Credit> credit = creditRepository.findByUserId(userId);
        return credit;
    }

    @Transactional
    @Override
    public Credit updareCredit(Credit credit) {
        creditRepository.saveAndFlush(credit);
        return credit;
    }

//    @Transactional(readOnly = true)
//    @Override
//    public Double getCreditAmountByUserId(Long userCustomerId) {
//        Credit credit = creditRepository.findByUserCustomerId(userCustomerId);
//        if (credit != null) {
//            return credit.getAmount();
//        } else {
//            throw new CreditNotFoundException("Credit not found for user with ID " + userCustomerId);
//        }
//    }
}
