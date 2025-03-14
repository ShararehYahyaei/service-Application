package org.example.serviceapplication.subService.repsitory;

import org.example.serviceapplication.subService.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
    List<SubCategory> findByPriceLessThan(double price);

}
