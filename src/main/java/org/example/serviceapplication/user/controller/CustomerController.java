package org.example.serviceapplication.user.controller;

import org.example.serviceapplication.credit.model.CreditDto;
import org.example.serviceapplication.credit.service.CreditService;
import org.example.serviceapplication.order.exception.OrderNotFound;
import org.example.serviceapplication.order.service.OrderService;
import org.example.serviceapplication.review.model.ReviewDto;
import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.order.model.OrderDto;
import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.request.dto.CustomerRequestResponseDto;
import org.example.serviceapplication.user.dto.SpecialistResponseDto;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.exception.UserHasWrongRole;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.customerService.CustomerService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final CreditService creditService;
    private final OrderService orderService;

    public CustomerController(CustomerService customerService, CreditService creditService, OrderService orderService) {
        this.customerService = customerService;
        this.creditService = creditService;
        this.orderService = orderService;
    }


    @PostMapping("/addRequest")
    public ResponseEntity createRequest(@RequestBody CustomerRequestDto customerRequest) {
        Long idUser = customerRequest.customerId();
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


        Sort sort = Sort.by(Sort.Direction.ASC, "offerPrice");
        List<OfferDto> allOffers = customerService.getAllOffers(customerRequestId, sort);
        allOffers.sort(Comparator
                .comparing(OfferDto::rate, Comparator.reverseOrder())
                .thenComparing(OfferDto::offerPrice));
        return allOffers;
    }


    @GetMapping("getAllRequest/{user_Id}")
    public ResponseEntity<List<CustomerRequestResponseDto>> getAllRequest(@PathVariable Long user_Id) {
        User customer = customerService.getUserById(user_Id);
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }

        return ResponseEntity.accepted().body(customerService.getAllRequests(user_Id));

    }


    @PostMapping("createOrder")
    public ResponseEntity createOrder(@RequestBody OrderDto orderDto) {
        Long idUser = orderDto.customerId();
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
    public ResponseEntity<SpecialistResponseDto> getMyCompletedOrder(@PathVariable Long userId,
                                                                     @PathVariable Long requestId) {
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


    @PostMapping("customer/createCredit")
    public ResponseEntity createCredit(@RequestBody CreditDto creditDto) {
        User customer = customerService.getUserById(creditDto.userCustomerId());
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }
        creditService.createCredit(creditDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Credit created successfully");
    }


    @GetMapping("customer/{userCustomerId}/credit")
    public ResponseEntity<Double> getCredit(@PathVariable Long userCustomerId) {
        User customer = customerService.getUserById(userCustomerId);
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }
        Double creditAmount = creditService.getCreditAmountByUserId(userCustomerId);
        return ResponseEntity.ok(creditAmount);
    }


    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerId(@PathVariable Long customerId) {
        User customer = customerService.getUserById(customerId);
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }
        List<OrderDto> ordersByCustomerId = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(ordersByCustomerId);
    }

}


