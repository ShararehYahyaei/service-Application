package org.example.serviceapplication.review.service;

import org.example.serviceapplication.review.model.ReviewDto;
import org.example.serviceapplication.user.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewService {
    void addReview(User customer, ReviewDto reviewDto);

    Double  getRateForSpecialist(Long specialistId);

    @Transactional
    Double getRateForUser(Long userId);
}
