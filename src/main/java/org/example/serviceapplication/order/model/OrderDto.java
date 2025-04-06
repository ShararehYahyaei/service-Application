package org.example.serviceapplication.order.model;

public record OrderDto(
        Long orderId,
       Long customerId,
       Long offerId,
       Long customerRequestId,
       OrderStatus status

) {
}
