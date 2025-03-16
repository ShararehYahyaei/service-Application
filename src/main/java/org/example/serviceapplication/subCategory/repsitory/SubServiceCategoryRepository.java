package org.example.serviceapplication.subCategory.repsitory;

import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubServiceCategoryRepository extends JpaRepository<SubServiceCategory,Long> {
    List<SubServiceCategory> findByPriceLessThan(double price);

}
