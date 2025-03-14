package org.example.serviceapplication.workCategory.service;

import ch.qos.logback.classic.spi.IThrowableProxy;
import org.example.serviceapplication.workCategory.exception.NotFoundWork;
import org.example.serviceapplication.workCategory.model.Work;
import org.example.serviceapplication.workCategory.repository.WorkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class WorkServiceImpl {

    public WorkServiceImpl(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    private final WorkRepository workRepository;


    public Work createNewCategory(Work categories) {
        return workRepository.save(categories);

    }

    public Work getWorkById(Long id) {
        return workRepository.findById(id)
                .orElseThrow(() -> new NotFoundWork("Work not found"));
    }


    public void deleteWork(long id) {
        workRepository.deleteById(id);

    }

    public List<Work> getAllWorks() {
        return workRepository.findAll();
    }

    public Work getWorkByName(String name) {
        return workRepository.getByName(name);
    }
}
