package org.example.serviceapplication.order.service;

import org.example.serviceapplication.order.model.Order;
import org.example.serviceapplication.order.model.OrderDto;
import org.example.serviceapplication.user.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {

    @Transactional
    void createOrder(User customer, OrderDto orderDto);

    @Transactional(readOnly = true)
    Order getOrderById(Long orderId);

    void changeOrderStatus(Long offerId);
}
