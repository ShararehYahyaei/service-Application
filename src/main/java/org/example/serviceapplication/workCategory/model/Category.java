package org.example.serviceapplication.workCategory.model;


import jakarta.persistence.*;
import lombok.*;
import org.example.serviceapplication.subService.model.SubCategory;

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
    private List<SubCategory> subCategotyList = new ArrayList<>();

    public Category(String name, List<SubCategory> subCategotyList) {
        this.name = name;
        this.subCategotyList = subCategotyList;
    }


}
