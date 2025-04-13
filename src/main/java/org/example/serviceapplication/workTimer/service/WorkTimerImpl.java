package org.example.serviceapplication.workTimer.service;

import org.example.serviceapplication.workTimer.model.WorkTimer;
import org.example.serviceapplication.workTimer.repository.WorkTimerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkTimerImpl implements WorkTimerService {
    private final WorkTimerRepository workTimerRepository;

    public WorkTimerImpl(WorkTimerRepository workTimerRepository) {
        this.workTimerRepository = workTimerRepository;
    }

    @Transactional
    @Override
    public WorkTimer creaetWorkTimer(WorkTimer workTimer){
        return workTimerRepository.save(workTimer);
    }
}
