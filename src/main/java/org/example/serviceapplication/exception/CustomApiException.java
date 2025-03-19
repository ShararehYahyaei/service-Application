package org.example.serviceapplication.exception;

public class CustomApiException extends RuntimeException {
    private final CustomApiExceptionType type;

    public CustomApiException(String message, CustomApiExceptionType type) {
        super(message);
        this.type = type;
    }

    public CustomApiExceptionType getType() {
        return this.type;
    }
}
