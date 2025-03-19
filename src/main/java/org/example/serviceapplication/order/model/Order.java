package org.example.serviceapplication.order.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User customer;
    @OneToOne(fetch = FetchType.LAZY)
    private Offer offer;
    @OneToOne(fetch = FetchType.LAZY)
    private CustomerRequest customerRequest;
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;


    public Order(User customer, Offer offer, CustomerRequest request) {
        this.customer = customer;
        this.offer = offer;
        this.customerRequest = request;
    }
}
