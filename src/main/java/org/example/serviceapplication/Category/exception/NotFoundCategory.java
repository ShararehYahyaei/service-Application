package org.example.serviceapplication.Category.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class NotFoundCategory extends CustomApiException {
    public NotFoundCategory(String message) {
        super(message, CustomApiExceptionType.NOT_FOUND);
    }
}
