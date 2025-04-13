package org.example.serviceapplication.order.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.example.serviceapplication.offer.exception.OfferNotFound;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.offer.model.OfferStatus;
import org.example.serviceapplication.order.exception.OrderIsDuplicated;
import org.example.serviceapplication.order.exception.OrderNotFound;
import org.example.serviceapplication.order.exception.OrderStatusIsNotCorrect;
import org.example.serviceapplication.order.model.Order;
import org.example.serviceapplication.order.model.OrderDto;
import org.example.serviceapplication.order.model.OrderDtoSearch;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @PersistenceContext
    private EntityManager entityManager;
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
        order.getCustomerRequest().setRequestStatus(RequestStatus.AwaitingSpecialistArrival);
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
                .map(order -> {
                    assert order.getOffer() != null;
                    return new OrderDto(
                            order.getId(),
                            order.getCustomer() != null ? order.getCustomer().getId() : null,
                            order.getOffer() != null ? order.getOffer().getId() : null,
                            order.getCustomerRequest() != null ? order.getCustomerRequest().getId() : null,
                            order.getOrderStatus(),
                            order.getOrderDate(),
                            order.getOffer() != null ? order.getOffer().getOfferPrice() : 0
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getAllOrdersForSpecialist(List<Long> specialistId) {

        List<Order> allByOfferIn = orderRepository.findAllByOfferIn(specialistId);
        return convertOrdersToOrderDtos(allByOfferIn);

    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> all = orderRepository.findAll();
        return convertOrdersToOrderDtos(all);
    }


    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> searchOrders(OrderDtoSearch orderDtoSearch) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> orderRoot = query.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();

        Join<Object, Object> customerRequestJoin = orderRoot.join("customerRequest");
        Join<Object, Object> subServiceJoin = customerRequestJoin.join("subServiceCategory");
        Join<Object, Object> serviceJoin = subServiceJoin.join("category");


        if (orderDtoSearch.subService() != null && !orderDtoSearch.subService().isEmpty()) {
            predicates.add(cb.like(cb.lower(subServiceJoin.get("name")), "%" + orderDtoSearch.subService()
                    .toLowerCase() + "%"));
        }

        if (orderDtoSearch.category() != null && !orderDtoSearch.category().isEmpty()) {
            predicates.add(cb.like(cb.lower(serviceJoin.get("name")), "%" + orderDtoSearch.category()
                    .toLowerCase() + "%"));
        }


        if (orderDtoSearch.orderStatus() != null && !orderDtoSearch.orderStatus().isEmpty()) {
            predicates.add(cb.like(orderRoot.get("orderStatus"), "%" + orderDtoSearch.orderStatus()
                    .toUpperCase() + "%"));
        }


        if (orderDtoSearch.fromLocalDate() != null && !orderDtoSearch.fromLocalDate().isBlank()
                &&
                orderDtoSearch.toLocalDate() != null && !orderDtoSearch.toLocalDate().isBlank()
        ) {

            predicates.add(cb.between(orderRoot.get("orderDate"),
                            LocalDateTime.of(LocalDate.parse(orderDtoSearch.fromLocalDate()),
                                    LocalTime.of(0, 0)),
                            LocalDateTime.of(LocalDate.parse(orderDtoSearch.toLocalDate()),
                                    LocalTime.of(23, 59))
                    )
            );

        }


        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return convertOrdersToOrderDtos(entityManager.createQuery(query).getResultList());
    }

    @Transactional(readOnly = true)
    @Override
    public Long countAllOrders() {
        return orderRepository.countByOrderStatus(OrderStatus.COMPLETED);

    }


}
