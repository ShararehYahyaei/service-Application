package org.example.serviceapplication.specialist.service;

import org.example.serviceapplication.Cusotmer.model.Customer;
import org.example.serviceapplication.enumPackage.Role;
import org.example.serviceapplication.enumPackage.Status;
import org.example.serviceapplication.specialist.model.Specialist;
import org.example.serviceapplication.specialist.repository.SpecialistRepository;
import org.example.serviceapplication.workCategory.model.Work;
import org.example.serviceapplication.workCategory.repository.WorkRepository;
import org.example.serviceapplication.workCategory.service.WorkServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
@SpringBootTest
class SpecialistServiceTest {

    @Autowired
    private SpecialistService service;
    @Autowired
    private SpecialistRepository specialistRepository;


    @Test
    void should_create_specialist() {

        Specialist specialist = getSpecialist();
        Specialist specialist1 = service.createSpecialist(specialist);
        assertNotNull(specialist1);


    }

    private Specialist getSpecialist() {
        byte[] imageBytes = "dummy image content".getBytes(StandardCharsets.UTF_8);
        MultipartFile imageFile = new MockMultipartFile("image", "image.jpg",
                "image/jpeg", imageBytes);

        return new Specialist("ali", "ali@gmail.com", "+989125478563",
                "3813", "123456", true, Role.Specialist, Status.newJoiner,
                imageBytes);
    }




}