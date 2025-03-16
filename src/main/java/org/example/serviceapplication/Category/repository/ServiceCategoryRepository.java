package org.example.serviceapplication.Category.repository;

import org.example.serviceapplication.Category.model.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Long> {

    List<ServiceCategory> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);

}
