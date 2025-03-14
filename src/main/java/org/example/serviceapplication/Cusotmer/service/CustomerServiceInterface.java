package org.example.serviceapplication.Cusotmer.service;

import org.example.serviceapplication.Cusotmer.dto.CustomerCreateDto;
import org.example.serviceapplication.Cusotmer.dto.CustomerResponseDto;
import org.example.serviceapplication.Cusotmer.model.Customer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerServiceInterface {


    @Transactional
    Customer createCustomer(CustomerCreateDto customerDto);

    @Transactional(readOnly = true)
    CustomerResponseDto findById(Long id);

    @Transactional(readOnly = true)
    List<CustomerResponseDto> getCustomersByName(String name);

    @Transactional(readOnly = true)
    List<CustomerResponseDto> getAllCustomers();

    @Transactional(readOnly = true)
    List<CustomerResponseDto> getAllCustomersWithActiveStatus();

    ;


}
