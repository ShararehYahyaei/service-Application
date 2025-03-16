package org.example.serviceapplication.subCategory.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.serviceapplication.Category.model.ServiceCategory;
import org.example.serviceapplication.user.model.User;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubServiceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;

    @ManyToOne
    private ServiceCategory category;
    @ManyToMany
    List<User> users=new ArrayList<>();

    public SubServiceCategory(String name, String description,
                              double price,
                              ServiceCategory category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;

    }

}
