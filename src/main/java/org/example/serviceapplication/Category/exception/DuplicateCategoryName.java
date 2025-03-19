package org.example.serviceapplication.Category.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class DuplicateCategoryName extends CustomApiException {
    public DuplicateCategoryName(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
