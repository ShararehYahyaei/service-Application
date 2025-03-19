package org.example.serviceapplication.user.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class UserHasNoAnyService extends CustomApiException {
    public UserHasNoAnyService(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
