package org.example.serviceapplication.workCategory.service;

import org.example.serviceapplication.Category.exception.NotFoundCategory;
import org.example.serviceapplication.Category.service.ServiceCategoryImpl;
import org.example.serviceapplication.Category.model.ServiceCategory;
import org.example.serviceapplication.Category.repository.ServiceCategoryRepository;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
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
class ServiceCategoryImplTest {
        @Autowired
        private ServiceCategoryImpl service;
        @Autowired
        private ServiceCategoryRepository serviceCategoryRepository;


    @Test
    void should_create_work_category() {
        ServiceCategory categories = getWork();
        ServiceCategory newCategory = service.createNewCategory(categories);
        Assertions.assertNotNull(newCategory);
        Assertions.assertNotNull(newCategory.getId());
        assertEquals(categories.getName(), newCategory.getName());


    }


    @Test
    void should_return_work_By_id() {
        ServiceCategory category = getWork();
        ServiceCategory newCategory = service.createNewCategory(category);
        ServiceCategory categoryById = service.getCategoryByIdResponse(newCategory.getId());
        assertEquals(categoryById.getId(), newCategory.getId());

    }

    @Test
    void should_return_exception_if_work_not_found() {
        ServiceCategory category = getWork();
        service.createNewCategory(category);
        NotFoundCategory notFoundCategory = assertThrows(NotFoundCategory.class, () -> service.getCategoryByIdResponse(30L));
        assertEquals("Work not found", notFoundCategory.getMessage());

    }


    @Test
    void should_delete_work_category() {
        ServiceCategory category = getWork();
        ServiceCategory newCategory = service.createNewCategory(category);
        service.deleteWork(newCategory.getId());
        NotFoundCategory notFoundCategory = assertThrows(NotFoundCategory.class, () -> service.getCategoryByIdResponse(newCategory.getId()));
        assertEquals("Work not found", notFoundCategory.getMessage());
    }

    @Test
    void should_return_all_works() {
        ServiceCategory category1 = getWork();
        ServiceCategory category2 = getWork();
        service.createNewCategory(category1);
        service.createNewCategory(category2);
        List<ServiceCategory> categories = service.getAllCategories();
        assertEquals(2, categories.size());

    }

    @Test
    void get_work_by_name() {
        ServiceCategory category = getWork();
        ServiceCategory newCategory = service.createNewCategory(category);
        ServiceCategory categoryByName = service.getAllCategoriesByName(newCategory.getName());
        assertEquals(category.getName(), categoryByName.getName());

    }

    private ServiceCategory getWork() {
        SubServiceCategory sSubServiceCategory1 = new SubServiceCategory("barghkari",
                "bargh is related to the house", 1500);
        SubServiceCategory sSubServiceCategory2 = new SubServiceCategory("barghkari",
                "bargh is related to the house", 1600);
        SubServiceCategory sSubServiceCategory3 = new SubServiceCategory("barghkari",
                "bargh is related to the house", 1700);
        List<SubServiceCategory> subCategories = new ArrayList<>();
        subCategories.add(sSubServiceCategory1);
        subCategories.add(sSubServiceCategory2);
        subCategories.add(sSubServiceCategory3);

        return new ServiceCategory("House", subCategories);
    }

    @AfterEach
    public void delete_table_After_each_test() {
        serviceCategoryRepository.deleteAll();
    }

}