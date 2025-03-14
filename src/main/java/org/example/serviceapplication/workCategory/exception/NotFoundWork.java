package org.example.serviceapplication.workCategory.exception;

public class NotFoundWork extends RuntimeException {
    public NotFoundWork(String message) {
        super(message);
    }
}
