package org.example.serviceapplication.Category.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    @NotEmpty(message = "null must not be null")
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<SubServiceCategory> subServiceCategoryList = new ArrayList<>();

    public ServiceCategory(String name) {
        this.name = name;
    }
}
