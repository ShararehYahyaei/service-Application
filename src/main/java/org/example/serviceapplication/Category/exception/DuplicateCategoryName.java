package org.example.serviceapplication.Category.exception;

public class DuplicateCategoryName extends RuntimeException {
    public DuplicateCategoryName(String message) {
        super(message);
    }
}
