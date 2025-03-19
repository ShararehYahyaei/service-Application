package org.example.serviceapplication.subCategory.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class SubServiceCategoryIsNotFound extends CustomApiException {
    public SubServiceCategoryIsNotFound(String message) {
        super(message, CustomApiExceptionType.NOT_FOUND);
    }
}
