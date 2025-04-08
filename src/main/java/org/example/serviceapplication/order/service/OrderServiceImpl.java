package org.example.serviceapplication.order.service;

import org.example.serviceapplication.offer.exception.OfferNotFound;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.offer.model.OfferStatus;
import org.example.serviceapplication.offer.service.OfferServiceInterface;
import org.example.serviceapplication.order.exception.OrderIsDuplicated;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRequestService customerRequestService;
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRequestService customerRequestService) {
        this.orderRepository = orderRepository;

        this.customerRequestService = customerRequestService;
    }

    @Transactional
    @Override
    public void createOrder(User customer, OrderDto orderDto) {
        logger.info("Create order");
        CustomerRequest request = customerRequestService.findRequestById(orderDto.customerRequestId());
        User customerForOrder = request.getUser();
        Optional<Offer> offer = request.getOffers().stream().filter(c -> c.getId().equals(orderDto.offerId())).
                findFirst();
        if (offer.isEmpty()) {
            throw new OfferNotFound("Offer not found");
        }
        boolean orderExists = orderRepository.existsByOffer(offer.get());
        if (orderExists) {
            throw new OrderIsDuplicated("An order with this offer already exists");
        }
        Order order = new Order(customerForOrder, offer.get(), request);
        order.setOrderDate(LocalDateTime.now());
        order.getOffer().setStatus(OfferStatus.ACCEPTED);
        order.getCustomerRequest().setRequestStatus(RequestStatus.InProgress);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);


    }

    private Order convertDtoToOrder(User customer, OrderDto orderDto) {
        logger.info("Convert order");
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
        logger.error("Order not found");
        throw new OrderNotFound("order customerRequestNumber not existed yeet...");
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
            logger.error("Order status is not correct");
            throw new OrderStatusIsNotCorrect("OrderStatusIsNotValid");
        }
        throw new OrderNotFound("order customerRequestNumber not existed yeet...");

    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if (orders.isEmpty()) {
            throw new OrderNotFound("order customerRequestNumber not existed yeet...");
        }
        return convertOrdersToOrderDtos(orders);

    }

    private static List<OrderDto> convertOrdersToOrderDtos(List<Order> orders) {
        return orders.stream()
                .map(order -> new OrderDto(
                        order.getId(),
                        order.getCustomer() != null ? order.getCustomer().getId() : null,
                        order.getOffer() != null ? order.getOffer().getId() : null,
                        order.getCustomerRequest() != null ? order.getCustomerRequest().getId() : null,
                        order.getOrderStatus()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto>getAllOrdersForSpecialist(List<Long> specialistId) {

        List<Order> allByOfferIn = orderRepository.findAllByOfferIn(specialistId);
        return convertOrdersToOrderDtos(allByOfferIn);

    }
}
