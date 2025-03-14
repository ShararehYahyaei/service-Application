package org.example.serviceapplication.specialist.repository;

import org.example.serviceapplication.specialist.model.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
}
