package org.example.serviceapplication.user.exception;

public class UserHasNoAnyService extends RuntimeException {
    public UserHasNoAnyService(String message) {
        super(message);
    }
}
