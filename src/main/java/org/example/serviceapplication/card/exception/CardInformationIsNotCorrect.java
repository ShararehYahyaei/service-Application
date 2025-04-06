package org.example.serviceapplication.card.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class CardInformationIsNotCorrect extends CustomApiException {
    public CardInformationIsNotCorrect(String message) {
      super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
