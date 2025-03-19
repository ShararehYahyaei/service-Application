package org.example.serviceapplication.order.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class OrderStatusIsNotCorrect extends CustomApiException {
    public OrderStatusIsNotCorrect(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
