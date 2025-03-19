package org.example.serviceapplication.user.service.customerService;

import org.example.serviceapplication.review.model.ReviewDto;
import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.offer.service.OfferServiceInterface;
import org.example.serviceapplication.order.model.OrderDto;
import org.example.serviceapplication.order.service.OrderService;
import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.request.dto.CustomerRequestResponseDto;
import org.example.serviceapplication.request.sercvice.CustomerRequestService;
import org.example.serviceapplication.review.service.ReviewService;
import org.example.serviceapplication.user.dto.CustomerResponseDto;
import org.example.serviceapplication.user.exception.UserNotFond;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.userRepository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CustomerService implements CustomerServiceInter {
    private final CustomerRequestService customerRequestService;
    private final OfferServiceInterface offerService;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final ReviewService reviewService;

    public CustomerService(UserRepository userRepository,
                           CustomerRequestService customerRequestService,
                           OfferServiceInterface offerService,
                           OrderService orderService,
                           ReviewService reviewService) {
        this.customerRequestService = customerRequestService;
        this.userRepository = userRepository;
        this.offerService = offerService;
        this.orderService = orderService;
        this.reviewService = reviewService;
    }


    @Transactional
    @Override
    public CustomerResponseDto createCustomer(User user) {
        User customer = userRepository.save(user);
        return convertEntityToResponseDto(customer);

    }


    public CustomerResponseDto convertEntityToResponseDto(User user) {
        return new CustomerResponseDto(
                user.getAddress(),
                user.getPhone(),
                user.getName(),
                user.isActive(),
                user.getRole(),
                user.getStatus()

        );
    }

    @Transactional
    @Override
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        throw new UserNotFond("User not found");

    }

    @Transactional
    @Override
    public void createRequest(User customer, CustomerRequestDto customerRequest) {
        customerRequestService.addRequest(customer, customerRequest);
    }

    @Transactional
    @Override
    public List<OfferDto> getAllOffers(Long requestId) {
        return offerService.getAllOffers(requestId);
    }

    @Override
    public List<CustomerRequestResponseDto> getAllRequests(Long userId) {
        return customerRequestService.getAllCustomerRequestDtoForCustomer(userId);
    }

    @Transactional
    @Override
    public void createOrder(User customer, OrderDto orderDto) {
        orderService.createOrder(customer, orderDto);
    }

    @Transactional
    @Override
    public void addReview(User customer, ReviewDto reviewDto) {
        reviewService.addReview(customer, reviewDto);
    }
    public List<CustomerResponseDto> convertEntitiesToResponseDtos(List<User> users) {
        return users.stream()
                .map(user -> new CustomerResponseDto(
                        user.getAddress(),
                        user.getPhone(),
                        user.getName(),
                        user.isActive(),
                        user.getRole(),
                        user.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
