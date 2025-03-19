package org.example.serviceapplication.user.controller;

import org.example.serviceapplication.review.model.ReviewDto;
import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.order.model.OrderDto;
import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.request.dto.CustomerRequestResponseDto;
import org.example.serviceapplication.user.dto.SpecialistResponseDto;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.exception.UserHasWrongRole;
import org.example.serviceapplication.user.exception.UserNotFond;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.customerService.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @PostMapping("/addRequest")
    public ResponseEntity createRequest(@RequestBody CustomerRequestDto customerRequest) {
        Long idUser = customerRequest.userId();
        User customer = customerService.getUserById(idUser);
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }
        customerService.createRequest(customer, customerRequest);
        return ResponseEntity.ok(HttpStatus.CREATED);

    }

    @GetMapping("/getAllOffers")
    public List<OfferDto> getAllOffers(
            @RequestParam(name = "user_id") Long userId,
            @RequestParam(name = "customer_request_id") Long customerRequestId) {
        User customer = customerService.getUserById(userId);
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }
        return customerService.getAllOffers(customerRequestId);
    }


    @GetMapping("getAllRequest/{user_Id}")
    public List<CustomerRequestResponseDto> getAllRequest(@PathVariable Long user_Id) {
        User customer = customerService.getUserById(user_Id);
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }
        return customerService.getAllRequests(user_Id);
    }


    @PostMapping("createOrder")
    public ResponseEntity createOrder(@RequestBody OrderDto orderDto) {
        Long idUser = orderDto.userId();
        User customer = customerService.getUserById(idUser);
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }
        customerService.createOrder(customer, orderDto);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }


    @PostMapping("giveReview")
    public ResponseEntity giveReview(@RequestBody ReviewDto reviewDto) {

        User customer = customerService.getUserById(reviewDto.customerId());
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }
        customerService.addReview(customer, reviewDto);
        return ResponseEntity.ok(HttpStatus.CREATED);

    }


    @GetMapping("getUserSpecialistForRating/{userId}/{requestId}")
    public ResponseEntity<SpecialistResponseDto> getMyCompletedOrder(@PathVariable Long userId, @PathVariable Long requestId) {
        User userById = customerService.getUserById(userId);
        if (userById.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }
        SpecialistResponseDto specialistForMyRequest = customerService.getSpecialistForMyRequest(requestId);
        return ResponseEntity.ok(specialistForMyRequest);

    }

    @PostMapping("customer/giveReview")
    public ResponseEntity<Void> gtReview(@RequestBody ReviewDto reviewDto) {
        User customer = customerService.getUserById(reviewDto.customerId());
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }

        customerService.addReview(customer, reviewDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
