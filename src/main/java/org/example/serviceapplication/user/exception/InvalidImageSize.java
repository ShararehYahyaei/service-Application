package org.example.serviceapplication.user.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class InvalidImageSize extends CustomApiException {
    public InvalidImageSize(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
