package org.example.serviceapplication.card.service;

import org.example.serviceapplication.card.model.Card;
import org.example.serviceapplication.card.model.CardDto;
import org.springframework.transaction.annotation.Transactional;

public interface CardService {
    @Transactional
    Card createCard(CardDto cardDto);
}
