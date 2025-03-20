package org.example.serviceapplication.request.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "request")
@Getter
@Setter
public class CustomerRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sub_service_category_id")
    private SubServiceCategory subServiceCategory;
    private double RequestPrice;
    private LocalDateTime RequestDate;
    private LocalDate deadLineTime;
    private String description;
    private String address;
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;
    @OneToMany(mappedBy = "customerRequest", fetch = FetchType.LAZY)
    private List<Offer> offers;

    public CustomerRequest(User userForRequest,
                           SubServiceCategory subServiceCategoryById,
                           double price, LocalDate localDate,
                           String description, String address
                         ) {
        this.user = userForRequest;
        this.subServiceCategory = subServiceCategoryById;
        this.RequestPrice = price;
        this.deadLineTime = localDate;
        this.description = description;
        this.address = address;
    }

    public CustomerRequest() {

    }


}
