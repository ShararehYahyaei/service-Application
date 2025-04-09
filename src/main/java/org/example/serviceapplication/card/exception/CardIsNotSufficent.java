package org.example.serviceapplication.card.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class CardIsNotSufficent extends CustomApiException {
    public CardIsNotSufficent(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
