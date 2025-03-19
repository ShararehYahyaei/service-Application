package org.example.serviceapplication.Category.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class NoActiveUsersFound extends CustomApiException {
    public NoActiveUsersFound(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
