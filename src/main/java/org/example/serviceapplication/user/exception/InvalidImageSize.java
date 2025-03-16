package org.example.serviceapplication.user.exception;

public class InvalidImageSize extends RuntimeException {
    public InvalidImageSize(String message) {
        super(message);
    }
}
