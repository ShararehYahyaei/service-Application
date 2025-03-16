package org.example.serviceapplication.Category.exception;

public class NoActiveUsersFound extends RuntimeException {
    public NoActiveUsersFound(String message) {
        super(message);
    }
}
