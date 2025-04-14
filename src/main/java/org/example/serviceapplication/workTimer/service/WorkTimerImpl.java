package org.example.serviceapplication.workTimer.service;

import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.workTimer.exception.NotWorkTimerForThisOffer;
import org.example.serviceapplication.workTimer.model.TimerStatus;
import org.example.serviceapplication.workTimer.model.WorkTimer;
import org.example.serviceapplication.workTimer.repository.WorkTimerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

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
        int durationInHours = offer.getEstimationTime();
        LocalDateTime end = start.plusHours(durationInHours);
        timer.setOffer(offer);
        timer.setStartTime(start);
        timer.setEndTime(end);
        timer.setTimerStatus(TimerStatus.RUNNING);
        timer.setEstimatedDurationInHours(durationInHours);
        return workTimerRepository.save(timer);

    }

    @Transactional
    public long calculateRemainingSeconds(WorkTimer timer) {
        return Duration.between(LocalDateTime.now(), timer.getEndTime()).getSeconds();
    }

    @Transactional
    @Override
    public String getRemainingTimeFormatted(WorkTimer timer) {
        long remaining = calculateRemainingSeconds(timer);

        if (remaining <= 0) {
            return "زمان به پایان رسیده است";
        }

        long hours = remaining / 3600;
        long minutes = (remaining % 3600) / 60;
        long seconds = remaining % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Transactional(readOnly = true)
    @Override
    public WorkTimer getByOffer(long offerId) {
        Optional<WorkTimer> byOffer = workTimerRepository.findByOffer_Id(offerId);
        if (byOffer.isPresent()) {
            return byOffer.get();
        } else {
            throw new NotWorkTimerForThisOffer("NotWorkTimerForThisOffer");
        }

    }

}
