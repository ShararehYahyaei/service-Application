package org.example.serviceapplication.location.service;

import org.example.serviceapplication.location.model.Location;
import org.example.serviceapplication.location.repository.LocationRepository;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    @Override
    public Location createLocation(double latitude, double longitude, CustomerRequest customerRequest) {
        Location location = new Location(latitude, longitude, customerRequest);
        return locationRepository.save(location);
    }

}
