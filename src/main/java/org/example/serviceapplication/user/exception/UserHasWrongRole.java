package org.example.serviceapplication.user.exception;

public class UserHasWrongRole extends RuntimeException {
    public UserHasWrongRole(String message) {
        super(message);
    }
}
