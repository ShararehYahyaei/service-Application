package org.example.serviceapplication.workTimer.service;

import org.example.serviceapplication.workTimer.model.WorkTimer;
import org.springframework.transaction.annotation.Transactional;

public interface WorkTimerService {
    @Transactional
    WorkTimer creaetWorkTimer(WorkTimer workTimer);
}
