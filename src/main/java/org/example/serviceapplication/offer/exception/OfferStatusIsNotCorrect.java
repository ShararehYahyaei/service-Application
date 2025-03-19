package org.example.serviceapplication.offer.exception;

public class OfferStatusIsNotCorrect extends RuntimeException {
    public OfferStatusIsNotCorrect(String message) {
        super(message);
    }
}
