package org.example.serviceapplication.order.model;

import java.time.LocalDateTime;

public record OrderDto(
        Long orderId,
       Long customerId,
       Long offerId,
       Long customerRequestId,
       OrderStatus status,
        LocalDateTime orderDate

) {
}
