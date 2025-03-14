package org.example.serviceapplication.specialist.service;


import org.example.serviceapplication.specialist.model.Specialist;
import org.example.serviceapplication.specialist.repository.SpecialistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SpecialistService {

    private final SpecialistRepository specialistRepository;
    public SpecialistService(SpecialistRepository specialistRepository) {
        this.specialistRepository = specialistRepository;
    }

    Specialist createSpecialist(Specialist specialist) {
        return specialistRepository.save(specialist);
    }

}
