package org.example.serviceapplication.workCategory.service;

import org.example.serviceapplication.workCategory.exception.NotFoundWork;
import org.example.serviceapplication.subService.model.SubCategory;
import org.example.serviceapplication.workCategory.model.Category;
import org.example.serviceapplication.workCategory.repository.CategoryRepository;
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
class CategoryServiceImplTest {
        @Autowired
        private CategoryServiceImpl service;
        @Autowired
        private CategoryRepository categoryRepository;


    @Test
    void should_create_work_category() {
        Category categories = getWork();
        Category newCategory = service.createNewCategory(categories);
        Assertions.assertNotNull(newCategory);
        Assertions.assertNotNull(newCategory.getId());
        assertEquals(categories.getName(), newCategory.getName());


    }


    @Test
    void should_return_work_By_id() {
        Category category = getWork();
        Category newCategory = service.createNewCategory(category);
        Category categoryById = service.getCategoryById(newCategory.getId());
        assertEquals(categoryById.getId(), newCategory.getId());

    }

    @Test
    void should_return_exception_if_work_not_found() {
        Category category = getWork();
        service.createNewCategory(category);
        NotFoundWork notFoundWork = assertThrows(NotFoundWork.class, () -> service.getCategoryById(30L));
        assertEquals("Work not found", notFoundWork.getMessage());

    }


    @Test
    void should_delete_work_category() {
        Category category = getWork();
        Category newCategory = service.createNewCategory(category);
        service.deleteWork(newCategory.getId());
        NotFoundWork notFoundWork = assertThrows(NotFoundWork.class, () -> service.getCategoryById(newCategory.getId()));
        assertEquals("Work not found", notFoundWork.getMessage());
    }

    @Test
    void should_return_all_works() {
        Category category1 = getWork();
        Category category2 = getWork();
        service.createNewCategory(category1);
        service.createNewCategory(category2);
        List<Category> categories = service.getAllWorks();
        assertEquals(2, categories.size());

    }

    @Test
    void get_work_by_name() {
        Category category = getWork();
        Category newCategory = service.createNewCategory(category);
        Category categoryByName = service.getWorkByName(newCategory.getName());
        assertEquals(category.getName(), categoryByName.getName());

    }

    private Category getWork() {
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

        return new Category("House", subCategories);
    }

    @AfterEach
    public void delete_table_After_each_test() {
        categoryRepository.deleteAll();
    }

}