package org.example.serviceapplication.request.repository;

import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CustomerRequestRepo extends JpaRepository<CustomerRequest, Long> {

    List<CustomerRequest> findAllBySubServiceCategoryIn
            (Collection<SubServiceCategory> subServiceCategories);
    List<CustomerRequest> findByUserId(Long userId);

}

