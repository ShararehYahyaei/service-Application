package org.example.serviceapplication.order.service;

import org.example.serviceapplication.order.model.Order;
import org.example.serviceapplication.order.model.OrderDto;
import org.example.serviceapplication.order.model.OrderDtoSearch;
import org.example.serviceapplication.user.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    @Transactional
    void createOrder(User customer, OrderDto orderDto);

    @Transactional(readOnly = true)
    Order getOrderById(Long orderId);

    void changeOrderStatus(Long offerId);

    @Transactional(readOnly = true)
    List<OrderDto> getOrdersByCustomerId(Long customerId);

    List<OrderDto>getAllOrdersForSpecialist(List<Long> specialistId);

    @Transactional(readOnly = true)
    List<OrderDto> getAllOrders();

    @Transactional(readOnly = true)
    List<OrderDto> searchOrders(OrderDtoSearch orderDtoSearch);
}
