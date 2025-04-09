package org.example.serviceapplication.card.service;

import org.example.serviceapplication.card.model.Card;
import org.example.serviceapplication.card.model.CardDto;
import org.example.serviceapplication.card.model.CardResponse;
import org.example.serviceapplication.user.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CardService {
    @Transactional
    Card createCard(CardDto cardDto);

    @Transactional
    boolean processPayment(Long selectedCard);
    List<CardResponse> getCardsByCustomerId(Long customerId);

    CardResponse getCardById(Long cardId);

    void widthraw(Long cardId, double amount, User user);

    @Transactional(readOnly = true)
    Card getByIdCard(Long cardId);
}
