package org.example.serviceapplication.payment;

import org.example.serviceapplication.credit.exception.CreditIsNotSufficent;
import org.example.serviceapplication.credit.exception.CreditNotFoundException;
import org.example.serviceapplication.credit.model.Credit;
import org.example.serviceapplication.credit.model.CreditDto;
import org.example.serviceapplication.credit.model.CreditStatus;
import org.example.serviceapplication.credit.service.CreditService;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.order.model.Order;
import org.example.serviceapplication.order.service.OrderService;
import org.example.serviceapplication.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
public class PaymentService implements PaymentServiceInterface {

    private final OrderService orderService;
    private final CreditService creditService;

    public PaymentService(OrderService orderService, CreditService creditService) {
        this.orderService = orderService;
        this.creditService = creditService;
    }

    @Transactional
    @Override
    public void payByCustomerCredit(Long orderId, Long customerId) {
        Order order = orderService.getOrderById(orderId);
        double offerPrice = order.getOffer().getOfferPrice();
        Offer offer = order.getOffer();
        User specialist = offer.getUser();
        Optional<Credit> credit = creditService.getCreditByUserId(customerId);
        if (credit.isEmpty()) {
            throw new CreditNotFoundException("Credit not found");
        }
        if (credit.get().getBalance() < offerPrice) {
            throw new CreditIsNotSufficent("credit is not sufficient to credit");
        }
        double amountNew = offerPrice * 0.70;
        credit.get().setBalance(credit.get().getBalance() -offerPrice);
        Optional<Credit> existingCredit = creditService.getCreditByUserId(specialist.getId());
        if (existingCredit.isPresent()) {
            Credit creditSpecialist = existingCredit.get();
            creditSpecialist.setBalance(creditSpecialist.getBalance() + amountNew);
            creditService.updareCredit(creditSpecialist);

        } else {
            CreditDto creditDto = new CreditDto(specialist.getId(), amountNew, CreditStatus.Active);
            creditService.createCredit(creditDto);

        }
    }
}
