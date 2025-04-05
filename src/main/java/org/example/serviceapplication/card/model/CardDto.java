package org.example.serviceapplication.card.model;

import java.time.LocalDate;

public record CardDto(
         String cardNumber,
          String cvv,
         LocalDate expirationDate,
         Double amount,
         Long customerId
) {

}
