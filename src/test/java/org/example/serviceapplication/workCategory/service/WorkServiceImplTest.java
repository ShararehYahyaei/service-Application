package org.example.serviceapplication.workCategory.service;

import org.example.serviceapplication.workCategory.exception.NotFoundWork;
import org.example.serviceapplication.subService.model.SubCategory;
import org.example.serviceapplication.workCategory.model.Work;
import org.example.serviceapplication.workCategory.repository.WorkRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ActiveProfiles("test")
@SpringBootTest
class WorkServiceImplTest {
        @Autowired
        private WorkServiceImpl service;
        @Autowired
        private WorkRepository workRepository;


    @Test
    void should_create_work_category() {
        Work categories = getWork();
        Work newCategory = service.createNewCategory(categories);
        Assertions.assertNotNull(newCategory);
        Assertions.assertNotNull(newCategory.getId());
        assertEquals(categories.getName(), newCategory.getName());


    }


    @Test
    void should_return_work_By_id() {
        Work work = getWork();
        Work newCategory = service.createNewCategory(work);
        Work workById = service.getWorkById(newCategory.getId());
        assertEquals(workById.getId(), newCategory.getId());

    }

    @Test
    void should_return_exception_if_work_not_found() {
        Work work = getWork();
        service.createNewCategory(work);
        NotFoundWork notFoundWork = assertThrows(NotFoundWork.class, () -> service.getWorkById(30L));
        assertEquals("Work not found", notFoundWork.getMessage());

    }


    @Test
    void should_delete_work_category() {
        Work work = getWork();
        Work newCategory = service.createNewCategory(work);
        service.deleteWork(newCategory.getId());
        NotFoundWork notFoundWork = assertThrows(NotFoundWork.class, () -> service.getWorkById(newCategory.getId()));
        assertEquals("Work not found", notFoundWork.getMessage());
    }

    @Test
    void should_return_all_works() {
        Work work1 = getWork();
        Work work2 = getWork();
        service.createNewCategory(work1);
        service.createNewCategory(work2);
        List<Work> works = service.getAllWorks();
        assertEquals(2, works.size());

    }

    @Test
    void get_work_by_name() {
        Work work = getWork();
        Work newCategory = service.createNewCategory(work);
        Work workByName = service.getWorkByName(newCategory.getName());
        assertEquals(work.getName(), workByName.getName());

    }

    private Work getWork() {
        SubCategory sCategory1 = new SubCategory("barghkari",
                "bargh is related to the house", 1500);
        SubCategory sCategory2 = new SubCategory("barghkari",
                "bargh is related to the house", 1600);
        SubCategory sCategory3 = new SubCategory("barghkari",
                "bargh is related to the house", 1700);
        List<SubCategory> subCategories = new ArrayList<>();
        subCategories.add(sCategory1);
        subCategories.add(sCategory2);
        subCategories.add(sCategory3);

        return new Work("House", subCategories);
    }

    @AfterEach
    public void delete_table_After_each_test() {
        workRepository.deleteAll();
    }

}