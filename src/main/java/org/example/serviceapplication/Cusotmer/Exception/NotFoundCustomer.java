package org.example.serviceapplication.Cusotmer.Exception;

public class NotFoundCustomer extends RuntimeException {
    public NotFoundCustomer(String message) {
        super(message);
    }
}
