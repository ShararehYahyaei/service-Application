package org.example.serviceapplication.review.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.serviceapplication.order.model.Order;
import org.example.serviceapplication.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User customer;

    @ManyToOne
    private User specialist;
    @OneToOne
    private Order order;

    @Min(1)
    @Max(5)
    private int rating;

    @Column(length = 1000)
    private String comment;

    private LocalDateTime reviewDate;


    public Review(User customer, Order order, int rating, String comment) {
        this.customer = customer;
        this.order = order;
        this.rating = rating;
        this.comment = comment;
    }


}
