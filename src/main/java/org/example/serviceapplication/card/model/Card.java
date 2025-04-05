package org.example.serviceapplication.card.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.example.serviceapplication.user.model.User;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(
            regexp = "\\d{16}",
            message = "Card number must be 16 digits"
    )
    private String cardNumber;

    @Pattern(
            regexp = "\\d{3,4}",
            message = "CVV must be 3 or 4 digits"
    )
    private String cvv;

    private LocalDate expirationDate;

    private Double amount;

    @ManyToOne
    private User user;

    public Card(String cardNumber, String cvv, LocalDate localDate, Double amount, User user) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expirationDate = localDate;
        this.amount = amount;
        this.user = user;
    }
}
