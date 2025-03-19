package org.example.serviceapplication.user.service.customerService;

import org.example.serviceapplication.review.model.ReviewDto;
import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.order.model.OrderDto;
import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.request.dto.CustomerRequestResponseDto;
import org.example.serviceapplication.user.dto.CustomerResponseDto;
import org.example.serviceapplication.user.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerServiceInter {
    @Transactional
    CustomerResponseDto createCustomer(User user);

    @Transactional
    User getUserById(Long id);

    @Transactional
    void createRequest(User customer, CustomerRequestDto customerRequest);

    @Transactional
    List<OfferDto> getAllOffers(Long requestId);

    List<CustomerRequestResponseDto> getAllRequests(Long userId);

    void createOrder(User customer, OrderDto orderDto);

    void addReview(User customer, ReviewDto reviewDto);
}
