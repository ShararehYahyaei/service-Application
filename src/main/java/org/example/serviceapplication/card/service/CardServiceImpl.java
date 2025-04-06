package org.example.serviceapplication.card.service;

import org.example.serviceapplication.card.exception.CardIsNotFound;
import org.example.serviceapplication.card.model.Card;
import org.example.serviceapplication.card.model.CardDto;
import org.example.serviceapplication.card.model.CardResponse;
import org.example.serviceapplication.card.repository.CardRepository;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Transactional(readOnly = true)
    @Override
    public List<CardResponse> getCardsByCustomerId(Long customerId) {
        List<Card> cards = cardRepository.findByUserId(customerId);
        return convertDtoToCardResponse(cards);
    }

    @Override
    public CardResponse getCardById(Long cardId) {
        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isEmpty()) {
            throw new CardIsNotFound("card is not found");
        }
       return convertCardToCardResponse(card.get());
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



    private List<CardResponse> convertDtoToCardResponse(List<Card>cards) {
        List<CardResponse> cardResponses = new ArrayList<>();
        for (Card card : cards) {
            CardResponse cardResponse = new CardResponse(
                    card.getId(),
                    card.getCardNumber(),
                    card.getAmount()

            );
            cardResponses.add(cardResponse);
        }
        return cardResponses;
    }



    private CardResponse convertCardToCardResponse(Card card) {
        return new CardResponse(
                card.getId(),
                card.getCardNumber(),
                card.getAmount());
    }

}
