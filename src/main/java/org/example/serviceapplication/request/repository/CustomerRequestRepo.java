package org.example.serviceapplication.request.repository;

import org.example.serviceapplication.request.model.CustomerRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRequestRepo extends JpaRepository<CustomerRequest, Long> {
}
