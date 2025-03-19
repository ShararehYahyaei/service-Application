package org.example.serviceapplication.request.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class RequestStatusIsNotCorrect extends CustomApiException {
    public RequestStatusIsNotCorrect(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
