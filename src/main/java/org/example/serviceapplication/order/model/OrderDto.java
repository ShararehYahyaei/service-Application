package org.example.serviceapplication.order.model;

public record OrderDto(
       Long userId,
       Long offerId,
       Long customerRequestId

) {
}
