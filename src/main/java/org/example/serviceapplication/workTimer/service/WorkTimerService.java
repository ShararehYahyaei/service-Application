package org.example.serviceapplication.workTimer.service;

import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.workTimer.model.WorkTimer;
import org.springframework.transaction.annotation.Transactional;

public interface WorkTimerService {


    @Transactional
    WorkTimer createWorkTimer(Offer offer);

    @Transactional
    String getRemainingTimeFormatted(WorkTimer timer);


    @Transactional(readOnly = true)
    WorkTimer getByOffer(long offerId);
}
