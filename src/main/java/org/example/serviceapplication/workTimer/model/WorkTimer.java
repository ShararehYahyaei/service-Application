package org.example.serviceapplication.workTimer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.serviceapplication.offer.model.Offer;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class WorkTimer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long estimatedDurationInHours;
    @Enumerated(EnumType.STRING)
    private  TimerStatus timerStatus;

    @Transient
    private long remainingTimeInSeconds;

    public WorkTimer(Offer offer, LocalDateTime startTime,
                     long estimatedDurationInHours,
                     TimerStatus timerStatus) {
        this.offer = offer;
        this.startTime = startTime;
        this.estimatedDurationInHours = estimatedDurationInHours;
        this.timerStatus = timerStatus;
    }

    public WorkTimer() {

    }


    public void updateRemainingTime() {
        this.remainingTimeInSeconds = Duration.between(LocalDateTime.now(),
                this.endTime).getSeconds();
    }

}
