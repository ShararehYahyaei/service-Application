package org.example.serviceapplication.subService.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.serviceapplication.workCategory.model.Category;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;

    @ManyToOne
    private Category category;

    public SubCategory(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;

    }

}
