package org.example.serviceapplication.offer.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class OfferNotFound extends CustomApiException {
    public OfferNotFound(String message) {
        super(message, CustomApiExceptionType.NOT_FOUND);
    }
}
