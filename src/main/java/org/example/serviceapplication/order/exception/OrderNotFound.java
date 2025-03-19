package org.example.serviceapplication.order.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class OrderNotFound extends CustomApiException {
    public OrderNotFound(String message) {
        super(message, CustomApiExceptionType.NOT_FOUND);
    }
}
