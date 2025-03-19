package org.example.serviceapplication.request.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class RequestNotPresent extends CustomApiException {
    public RequestNotPresent(String message) {
        super(message, CustomApiExceptionType.NOT_FOUND);
    }
}
