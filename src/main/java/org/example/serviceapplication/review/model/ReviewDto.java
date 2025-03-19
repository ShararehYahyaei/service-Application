package org.example.serviceapplication.review.model;

public record ReviewDto(
        Long customerId,
        Long userId,
        Long orderId,
        int rating,
        String comment
) {
}
