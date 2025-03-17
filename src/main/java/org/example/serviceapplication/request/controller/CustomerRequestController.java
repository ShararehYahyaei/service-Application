package org.example.serviceapplication.request.controller;

import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.request.sercvice.CustomerRequestInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/request")
public class CustomerRequestController {
    private final CustomerRequestInterface customerRequestService;

    public CustomerRequestController(CustomerRequestInterface customerRequest) {
        this.customerRequestService = customerRequest;
    }


    //todo inject service interface
    //todo create a request
    //todo update status request based on offer specialist
    //todo update request based on status request


    @PostMapping("/addRequest")
    public ResponseEntity createRequest(@RequestBody CustomerRequestDto customerRequest) {
        customerRequestService.addRequest(customerRequest);
        return ResponseEntity.ok(HttpStatus.CREATED);

    }


}
