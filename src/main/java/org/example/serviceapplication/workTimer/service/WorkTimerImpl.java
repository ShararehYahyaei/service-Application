package org.example.serviceapplication.workTimer.service;

import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.workTimer.model.TimerStatus;
import org.example.serviceapplication.workTimer.model.WorkTimer;
import org.example.serviceapplication.workTimer.repository.WorkTimerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WorkTimerImpl implements WorkTimerService {
    private final WorkTimerRepository workTimerRepository;

    public WorkTimerImpl(WorkTimerRepository workTimerRepository) {
        this.workTimerRepository = workTimerRepository;
    }

    @Transactional
    @Override
    public WorkTimer createWorkTimer(Offer offer) {
        WorkTimer timer = new WorkTimer();
        LocalDateTime start = LocalDateTime.now();

        int durationInDays = offer.getEstimationTime();
        LocalDateTime end = start.plusDays(durationInDays);
        timer.setOffer(offer);
        timer.setStartTime(start);
        timer.setEndTime(end);
        timer.setTimerStatus(TimerStatus.RUNNING);
        return workTimerRepository.save(timer);

    }
}
