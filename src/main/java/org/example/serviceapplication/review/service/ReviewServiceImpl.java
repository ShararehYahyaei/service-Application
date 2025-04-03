package org.example.serviceapplication.review.service;

import org.example.serviceapplication.order.exception.OrderOwnershipException;
import org.example.serviceapplication.order.exception.OrderStatusIsNotCorrect;
import org.example.serviceapplication.order.model.Order;
import org.example.serviceapplication.order.model.OrderStatus;
import org.example.serviceapplication.order.service.OrderService;
import org.example.serviceapplication.review.model.Review;
import org.example.serviceapplication.review.model.ReviewDto;
import org.example.serviceapplication.review.repository.ReviewRepository;
import org.example.serviceapplication.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final OrderService orderService;
    private final ReviewRepository reviewRepository;
    private final Logger logger = LoggerFactory.getLogger(ReviewServiceImpl.class);


    public ReviewServiceImpl(OrderService orderService, ReviewRepository reviewRepository
    ) {
        this.orderService = orderService;
        this.reviewRepository = reviewRepository;

    }

    @Transactional
    @Override
    public void addReview(User customer, ReviewDto reviewDto) {
        logger.info("Adding review");
        Review review = convertRequestIntoEntity(customer, reviewDto);
        review.setReviewDate(LocalDateTime.now());
        reviewRepository.save(review);
    }


    private Review convertRequestIntoEntity(User customer, ReviewDto reviewDto) {
        Order order = orderService.getOrderById(reviewDto.orderId());


        if (!order.getCustomer().getId().equals(customer.getId())) {
            logger.error("Order does not belong to this customer!");
            throw new OrderOwnershipException("Order does not belong to this customer.");
        }
        if (order.getOrderStatus() != OrderStatus.COMPLETED) {
            logger.error("Order Status Is Not Correct");
            throw new OrderStatusIsNotCorrect("Order status must be COMPLETED to leave a review.");
        }

        return new Review(
                customer,
                order,
                reviewDto.rating(),
                reviewDto.comment()

        );


    }

    @Override
    public Double getRateForSpecialist(Long specialistId) {
        return reviewRepository.findAverageRatingBySpecialistId(specialistId);
    }

    @Transactional
    @Override
    public Double getRateForUser(Long userId) {
        Double rate = reviewRepository.findAverageRatingByUserId(userId);
        return rate != null ? rate : 0.0;
    }

}
