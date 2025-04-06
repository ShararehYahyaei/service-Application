package org.example.serviceapplication.card.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class CardIsNotFound extends CustomApiException {
    public CardIsNotFound(String message) {
        super(message, CustomApiExceptionType.NOT_FOUND);
    }
}
