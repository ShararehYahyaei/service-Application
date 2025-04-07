package org.example.serviceapplication.card.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class CardIsExpired extends CustomApiException {
    public CardIsExpired(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
