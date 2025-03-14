package org.example.serviceapplication.workCategory.repository;

import org.example.serviceapplication.workCategory.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    Work getByName(String name);

}
