package org.example.serviceapplication.card.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class CardValidationException extends CustomApiException {
    public CardValidationException(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
