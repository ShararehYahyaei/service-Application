package org.example.serviceapplication.credit.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;
import org.example.serviceapplication.request.sercvice.CustomerRequestService;

public class CreditNotFoundException extends CustomApiException {
    public CreditNotFoundException(String message) {
        super(message, CustomApiExceptionType.NOT_FOUND);
    }
}
