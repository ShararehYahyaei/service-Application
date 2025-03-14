package org.example.serviceapplication.Cusotmer.controller;


import org.example.serviceapplication.Cusotmer.dto.CustomerCreateDto;
import org.example.serviceapplication.Cusotmer.dto.CustomerResponseDto;
import org.example.serviceapplication.Cusotmer.model.Customer;
import org.example.serviceapplication.Cusotmer.service.CustomerService;
import org.example.serviceapplication.Cusotmer.service.CustomerServiceInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseStatus;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/customer")

public class CustomerController {


    private final CustomerServiceInterface customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUpCustomer(@Valid @RequestBody CustomerCreateDto customerDto) {
        customerService.createCustomer(customerDto);


    }


    @GetMapping("/getAllCustomers")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<CustomerResponseDto> allCustomers = customerService.getAllCustomers();
        return new ResponseEntity<>(allCustomers, HttpStatus.OK);
    }


    @GetMapping("/getActiveCustomers")
    public ResponseEntity<List<CustomerResponseDto>> getActiveCustomers() {
        List<CustomerResponseDto> allCustomers = customerService.getAllCustomersWithActiveStatus();
        return new ResponseEntity<>(allCustomers, HttpStatus.OK);
    }

    @GetMapping("/getCustomersByName")
    public ResponseEntity<List<CustomerResponseDto>> getCustomersByName(@RequestParam String name) {
        List<CustomerResponseDto> allCustomers = customerService.getCustomersByName(name);
        return new ResponseEntity<>(allCustomers, HttpStatus.OK);
    }


}
