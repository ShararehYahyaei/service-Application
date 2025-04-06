package org.example.serviceapplication.card.model;

import java.time.LocalDate;

public record CardResponse(
        Long cardId,
        String cardNumber,
        Double balance
) {
}
