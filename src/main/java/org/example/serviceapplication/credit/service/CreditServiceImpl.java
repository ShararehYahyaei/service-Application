package org.example.serviceapplication.credit.service;

import org.example.serviceapplication.credit.exception.CreditNotFoundException;
import org.example.serviceapplication.credit.model.Credit;
import org.example.serviceapplication.credit.model.CreditDto;
import org.example.serviceapplication.credit.model.CreditStatus;
import org.example.serviceapplication.credit.repository.CreditRepository;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.user.dto.UserResponseDto;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditServiceImpl implements CreditService {
    private final CreditRepository creditRepository;
    private final UserService userService;

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
        User user = userService.getUserById(creditDto.userId());
        return new Credit(
               user,
                creditDto.balance(),
                creditDto.expirationDate()
        );

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
