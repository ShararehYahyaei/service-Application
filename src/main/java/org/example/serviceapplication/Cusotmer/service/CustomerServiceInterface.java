package org.example.serviceapplication.Cusotmer.service;

import org.example.serviceapplication.Cusotmer.dto.CustomerCreateDto;
import org.example.serviceapplication.Cusotmer.dto.CustomerResponseDto;
import org.example.serviceapplication.Cusotmer.model.Customer;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerServiceInterface {



    Customer createCustomer(CustomerCreateDto customerDto);


    CustomerResponseDto findById(Long id);


    List<CustomerResponseDto> getCustomersByName(String name);


    List<CustomerResponseDto> getAllCustomers();


    List<CustomerResponseDto> getAllCustomersWithActiveStatus();

    ;


}
