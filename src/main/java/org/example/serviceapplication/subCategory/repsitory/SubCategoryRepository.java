package org.example.serviceapplication.subCategory.repsitory;

import org.example.serviceapplication.subCategory.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
    List<SubCategory> findByPriceLessThan(double price);

}
