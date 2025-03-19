package org.example.serviceapplication.user.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class UserNotFond extends CustomApiException {
    public UserNotFond(String message) {
        super(message, CustomApiExceptionType.NOT_FOUND);
    }
}
