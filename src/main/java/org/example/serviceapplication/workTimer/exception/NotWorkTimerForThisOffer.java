package org.example.serviceapplication.workTimer.exception;

import org.example.serviceapplication.exception.CustomApiException;
import org.example.serviceapplication.exception.CustomApiExceptionType;

public class NotWorkTimerForThisOffer extends CustomApiException {
    public NotWorkTimerForThisOffer(String message) {
        super(message, CustomApiExceptionType.BAD_REQUEST);
    }
}
