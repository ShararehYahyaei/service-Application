package org.example.serviceapplication.location.service;

import org.example.serviceapplication.location.model.Location;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.springframework.transaction.annotation.Transactional;

public interface LocationService {

    @Transactional
    Location createLocation(double latitude, double longitude, CustomerRequest customerRequest);
}
