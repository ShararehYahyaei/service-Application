package org.example.serviceapplication.offer.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "offer")
@Getter
@Setter
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User user;
    private double offerPrice;
    private LocalDateTime createTime;
    private LocalDate offerDate;
    private int estimationTime;
    @ManyToOne
    private CustomerRequest customerRequest;
    @Enumerated(EnumType.STRING)
    OfferStatus status;


    public Offer(User specialist,
                 CustomerRequest requestOne,
                 double offerPrice,
                 LocalDate offerDate,
                 int estimationTime) {
        this.user = specialist;
        this.customerRequest = requestOne;
        this.offerPrice = offerPrice;
        this.offerDate = offerDate;
        this.estimationTime = estimationTime;
    }

    public Offer() {

    }


    public Offer(double price, LocalDate localDate, int estimationTime) {
        this.offerPrice = price;
        this.offerDate = localDate;
        this.estimationTime = estimationTime;
    }


}
