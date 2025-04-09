package org.example.serviceapplication.user.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class DuplicateSubCategoryException extends CustomApiException {
    public DuplicateSubCategoryException(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
