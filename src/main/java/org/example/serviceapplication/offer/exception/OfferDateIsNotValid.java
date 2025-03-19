package org.example.serviceapplication.offer.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class OfferDateIsNotValid extends CustomApiException {
    public OfferDateIsNotValid(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
