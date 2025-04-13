package org.example.serviceapplication.workTimer.repository;

import org.example.serviceapplication.workTimer.model.WorkTimer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkTimerRepository extends JpaRepository<WorkTimer, Long> {
}
