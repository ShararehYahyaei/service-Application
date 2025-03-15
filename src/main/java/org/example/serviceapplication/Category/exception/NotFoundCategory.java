package org.example.serviceapplication.Category.exception;

public class NotFoundCategory extends RuntimeException {
    public NotFoundCategory(String message) {
        super(message);
    }
}
