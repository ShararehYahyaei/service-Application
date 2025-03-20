package org.example.serviceapplication.order.service;

import org.example.serviceapplication.offer.exception.OfferNotFound;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.offer.model.OfferStatus;
import org.example.serviceapplication.offer.service.OfferServiceInterface;
import org.example.serviceapplication.order.exception.OrderNotFound;
import org.example.serviceapplication.order.exception.OrderStatusIsNotCorrect;
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
    private final CustomerRequestService customerRequestService;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRequestService customerRequestService) {
        this.orderRepository = orderRepository;

        this.customerRequestService = customerRequestService;
    }

    @Transactional
    @Override
    public void createOrder(User customer, OrderDto orderDto) {
        CustomerRequest request = customerRequestService.findRequestById(orderDto.customerRequestId());
        User customerForOrder = request.getUser();
        Optional<Offer> offer = request.getOffers().stream().filter(c -> c.getId().equals(orderDto.offerId())).
                findFirst();
        if (offer.isEmpty()) {
            throw new OfferNotFound("Offer not found");
        } else {
            Order order = new Order(customerForOrder, offer.get(), request);
            order.setOrderDate(LocalDateTime.now());
            order.getOffer().setStatus(OfferStatus.ACCEPTED);
            order.getCustomerRequest().setRequestStatus(RequestStatus.InProgress);
            order.setOrderStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
        }


    }

    private Order convertDtoToOrder(User customer, OrderDto orderDto) {
        CustomerRequest request = customerRequestService.findRequestById(orderDto.customerRequestId());
        if (request.getRequestStatus() == RequestStatus.AwaitingSelection) {
            return new Order(
                    customer,
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

    @Transactional
    @Override
    public void changeOrderStatus(Long offerId) {

        Optional<Order> byOfferId = orderRepository.findByOfferId(offerId);
        if (byOfferId.isPresent()) {
            Order order = byOfferId.get();
            if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
                order.setOrderStatus(OrderStatus.COMPLETED);
                orderRepository.save(order);
                return;
            }
            throw new OrderStatusIsNotCorrect("OrderStatusIsNotValid");
        }
        throw new OrderNotFound("order id not existed yeet...");

    }
}
