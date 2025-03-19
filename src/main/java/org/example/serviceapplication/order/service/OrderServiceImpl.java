package org.example.serviceapplication.order.service;

import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.offer.model.OfferStatus;
import org.example.serviceapplication.offer.service.OfferServiceInterface;
import org.example.serviceapplication.order.exception.OrderNotFound;
import org.example.serviceapplication.order.model.Order;
import org.example.serviceapplication.order.model.OrderDto;
import org.example.serviceapplication.order.model.OrderStatus;
import org.example.serviceapplication.order.repository.OrderRepository;
import org.example.serviceapplication.request.exception.RequestStatusIsNotCorrect;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.request.model.RequestStatus;
import org.example.serviceapplication.request.sercvice.CustomerRequestService;
import org.example.serviceapplication.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OfferServiceInterface offerService;
    private final CustomerRequestService customerRequestService;

    public OrderServiceImpl(OrderRepository orderRepository, OfferServiceInterface offerService, CustomerRequestService customerRequestService) {
        this.orderRepository = orderRepository;
        this.offerService = offerService;
        this.customerRequestService = customerRequestService;
    }

    @Transactional
    @Override
    public void createOrder(User customer, OrderDto orderDto) {
        Order order = convertDtoToEntity(customer, orderDto);
        order.setOrderDate(LocalDateTime.now());
        order.getOffer().setStatus(OfferStatus.ACCEPTED);
        order.getCustomerRequest().setRequestStatus(RequestStatus.InProgress);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
    }

    private Order convertDtoToEntity(User customer, OrderDto orderDto) {
        Offer offer = offerService.getOfferById(orderDto.offerId());
        CustomerRequest request = customerRequestService.findRequestById(orderDto.customerRequestId());
        if (request.getRequestStatus() == RequestStatus.AwaitingSelection) {
            return new Order(
                    customer,
                    offer,
                    request
            );
        } else {
            throw new RequestStatusIsNotCorrect("Request status is not correct");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Order getOrderById(Long orderId) {
        Optional<Order> found = orderRepository.findById(orderId);
        if (found.isPresent()) {
            return found.get();
        }
        throw new OrderNotFound("order id not existed yeet...");
    }
}
