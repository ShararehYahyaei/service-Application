package org.example.serviceapplication.credit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.serviceapplication.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "credit")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private Double balance;
    @Enumerated(EnumType.STRING)
    private CreditStatus status;
    public Credit(User user, Double balance ) {
        this.user = user;
        this.balance = balance;

    }

    public Credit() {
    }
}
