package org.example.serviceapplication.order.exception;

public class OrderStatusIsNotCorrect extends RuntimeException {
    public OrderStatusIsNotCorrect(String message) {
        super(message);
    }
}
