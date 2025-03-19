package org.example.serviceapplication.offer.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class OfferStatusIsNotCorrect extends CustomApiException {
    public OfferStatusIsNotCorrect(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
