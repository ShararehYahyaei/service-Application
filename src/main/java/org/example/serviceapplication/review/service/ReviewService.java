package org.example.serviceapplication.review.service;

import org.example.serviceapplication.review.model.ReviewDto;
import org.example.serviceapplication.user.model.User;

public interface ReviewService {
    void addReview(User customer, ReviewDto reviewDto);

    Double  getRateForSpecialist(Long specialistId);
}
