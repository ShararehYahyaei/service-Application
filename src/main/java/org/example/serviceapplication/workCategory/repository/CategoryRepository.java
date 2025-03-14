package org.example.serviceapplication.workCategory.repository;

import org.example.serviceapplication.Cusotmer.model.Customer;
import org.example.serviceapplication.workCategory.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByNameContainingIgnoreCase(String name);

}
