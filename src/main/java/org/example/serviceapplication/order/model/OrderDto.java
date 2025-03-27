package org.example.serviceapplication.order.model;

public record OrderDto(
       Long customerId,
       Long offerId,
       Long customerRequestId

) {
}
