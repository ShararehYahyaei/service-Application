package org.example.serviceapplication.card.service;

import org.example.serviceapplication.card.model.Card;
import org.example.serviceapplication.card.model.CardDto;
import org.example.serviceapplication.card.repository.CardRepository;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final UserService userService;

    public CardServiceImpl(CardRepository cardRepository, UserService userService) {
        this.cardRepository = cardRepository;
        this.userService = userService;
    }


    @Transactional
    @Override
    public Card createCard(CardDto cardDto) {
        Card card = converDtoToCard(cardDto);
        return cardRepository.save(card);
    }

    @Transactional
    @Override
    public boolean processPayment(Long selectedCard) {
        return true;
    }

    private Card converDtoToCard(CardDto cardDto) {
        User user = userService.getUserById(cardDto.customerId());
        return new Card(
                cardDto.cardNumber(),
                cardDto.cvv(),
                cardDto.expirationDate(),
                cardDto.amount(),
                user

        );
    }


}
