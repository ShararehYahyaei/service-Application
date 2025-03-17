package org.example.serviceapplication.user.exception;

public class UserAlreadyHasThisSubService extends RuntimeException {
    public UserAlreadyHasThisSubService(String message) {
        super(message);
    }
}
