package org.example.serviceapplication.user.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class NotSubServiceCategory extends CustomApiException {
    public NotSubServiceCategory(String message) {
        super(message, CustomApiExceptionType.NOT_FOUND);
    }
}
