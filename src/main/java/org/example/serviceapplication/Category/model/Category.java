package org.example.serviceapplication.Category.model;


import jakarta.persistence.*;
import lombok.*;
import org.example.serviceapplication.subCategory.model.SubCategory;
import org.example.serviceapplication.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<SubCategory> subCategoryList = new ArrayList<>();
    @ManyToMany
    private List<User>users = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }
}
