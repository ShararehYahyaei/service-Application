package org.example.serviceapplication.card.service;

import org.example.serviceapplication.card.exception.CardIsNotFound;
import org.example.serviceapplication.card.model.Card;
import org.example.serviceapplication.card.model.CardDto;
import org.example.serviceapplication.card.model.CardResponse;
import org.example.serviceapplication.card.repository.CardRepository;
import org.example.serviceapplication.credit.model.Credit;
import org.example.serviceapplication.credit.model.CreditDto;
import org.example.serviceapplication.credit.model.CreditStatus;
import org.example.serviceapplication.credit.repository.CreditRepository;
import org.example.serviceapplication.credit.service.CreditService;
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
    private final CreditService creditService;



    public CardServiceImpl(CardRepository cardRepository, UserService userService, CreditService creditService) {
        this.cardRepository = cardRepository;
        this.userService = userService;

        this.creditService = creditService;
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

    @Transactional
    @Override
    public void widthraw(Long cardId, double amount, User user) {
        Optional<Card> cardFound = cardRepository.findById(cardId);
        double deductionAmount = 0.0;
        if (cardFound.isPresent()) {
            deductionAmount = amount * 0.70;
            cardFound.get().setAmount(cardFound.get().getAmount() - amount);
            Optional<Credit> existingCredit = creditService.getCreditByUserId(user.getId());
            if (existingCredit.isPresent()) {
                Credit credit = existingCredit.get();
                credit.setBalance(credit.getBalance() + deductionAmount);
                creditService.updareCredit(credit);
                cardRepository.save(cardFound.get());
            } else {
                CreditDto creditDto = new CreditDto(user.getId(), deductionAmount, CreditStatus.Active);
                creditService.createCredit(creditDto);
                cardRepository.save(cardFound.get());
            }

        }else{
            throw new CardIsNotFound("card is not found");

        }

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


    private List<CardResponse> convertDtoToCardResponse(List<Card> cards) {
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

    @Transactional(readOnly = true)
    @Override
    public Card getByIdCard(Long cardId) {
        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isEmpty()) {
            throw new CardIsNotFound("card is not found");
        }
        return card.get();
    }

}
