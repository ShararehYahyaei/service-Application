package org.example.serviceapplication.subService.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.serviceapplication.workCategory.model.Work;


@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;

    @ManyToOne
    private Work work;

    public SubCategory(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;

    }

}
