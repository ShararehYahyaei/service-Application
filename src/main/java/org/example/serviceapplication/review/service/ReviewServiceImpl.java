package org.example.serviceapplication.review.service;

import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.order.exception.OrderStatusIsNotCorrect;
import org.example.serviceapplication.order.model.Order;
import org.example.serviceapplication.order.model.OrderStatus;
import org.example.serviceapplication.order.service.OrderService;
import org.example.serviceapplication.request.exception.RequestStatusIsNotCorrect;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.request.model.RequestStatus;
import org.example.serviceapplication.review.model.Review;
import org.example.serviceapplication.review.model.ReviewDto;
import org.example.serviceapplication.review.repository.ReviewRepository;
import org.example.serviceapplication.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final OrderService orderService;
    private final ReviewRepository reviewRepository;

    public ReviewServiceImpl(OrderService orderService, ReviewRepository reviewRepository) {
        this.orderService = orderService;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    @Override
    public void addReview(User customer, ReviewDto reviewDto) {
        Review review = convertRequestIntoEntity(customer, reviewDto);
        review.setReviewDate(LocalDateTime.now());
        reviewRepository.save(review);
    }


    private Review convertRequestIntoEntity(User customer, ReviewDto reviewDto) {
        Order order = orderService.getOrderById(reviewDto.orderId());
        if (order.getOrderStatus() == OrderStatus.COMPLETED) {
            return new Review(
                    customer,
                    order,
                    reviewDto.rating(),
                    reviewDto.comment()

            );
        }
        throw new OrderStatusIsNotCorrect("OrderStatusIsNotCorrect");
    }
}
