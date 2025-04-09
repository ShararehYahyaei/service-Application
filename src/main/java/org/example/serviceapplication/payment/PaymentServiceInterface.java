package org.example.serviceapplication.payment;

import org.springframework.transaction.annotation.Transactional;

public interface PaymentServiceInterface {
    @Transactional
    void payByCustomerCredit(Long orderId, Long customerId);
}
