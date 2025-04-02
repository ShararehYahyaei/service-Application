package org.example.serviceapplication.order.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class OrderOwnershipException extends CustomApiException {
    public OrderOwnershipException(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
