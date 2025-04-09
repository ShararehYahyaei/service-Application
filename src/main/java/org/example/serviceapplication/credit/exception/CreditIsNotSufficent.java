package org.example.serviceapplication.credit.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class CreditIsNotSufficent extends CustomApiException {
    public CreditIsNotSufficent(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
