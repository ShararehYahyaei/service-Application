package org.example.serviceapplication.request.exception;

public class RequestStatusIsNotCorrect extends RuntimeException {
    public RequestStatusIsNotCorrect(String message) {
        super(message);
    }
}
