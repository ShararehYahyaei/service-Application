package org.example.serviceapplication.workCategory.model;


import jakarta.persistence.*;
import lombok.*;
import org.example.serviceapplication.subService.model.SubCategory;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor

public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "work", cascade = CascadeType.ALL)
    private List<SubCategory> subCategotyList = new ArrayList<>();

    public Work(String name, List<SubCategory> subCategotyList) {
        this.name = name;
        this.subCategotyList = subCategotyList;
    }

    public Work() {
    }

    public void setSubCategoryList(List<SubCategory> subCategotyList) {
        this.subCategotyList = subCategotyList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubCategory> getSubCategotyList() {
        return subCategotyList;
    }

    public void setSubCategotyList(List<SubCategory> subCategotyList) {
        this.subCategotyList = subCategotyList;
    }
}
