package org.example.serviceapplication.location.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.serviceapplication.request.model.CustomerRequest;

@Entity

@Getter
@Setter

public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double latitude;
    private double longitude;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_request_id")
    private CustomerRequest customerRequest;

    public Location(double latitude, double longitude, CustomerRequest customerRequest) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.customerRequest = customerRequest;
    }

    public Location() {
    }
}
