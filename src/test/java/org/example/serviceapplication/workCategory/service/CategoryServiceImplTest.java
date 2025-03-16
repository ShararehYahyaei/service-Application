package org.example.serviceapplication.workCategory.service;

import org.example.serviceapplication.Category.exception.NotFoundCategory;
import org.example.serviceapplication.Category.service.CategoryServiceImpl;
import org.example.serviceapplication.subCategory.model.SubCategory;
import org.example.serviceapplication.Category.model.Category;
import org.example.serviceapplication.Category.repository.CategoryRepository;
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
        Category categoryById = service.getCategoryByIdResponse(newCategory.getId());
        assertEquals(categoryById.getId(), newCategory.getId());

    }

    @Test
    void should_return_exception_if_work_not_found() {
        Category category = getWork();
        service.createNewCategory(category);
        NotFoundCategory notFoundCategory = assertThrows(NotFoundCategory.class, () -> service.getCategoryByIdResponse(30L));
        assertEquals("Work not found", notFoundCategory.getMessage());

    }


    @Test
    void should_delete_work_category() {
        Category category = getWork();
        Category newCategory = service.createNewCategory(category);
        service.deleteWork(newCategory.getId());
        NotFoundCategory notFoundCategory = assertThrows(NotFoundCategory.class, () -> service.getCategoryByIdResponse(newCategory.getId()));
        assertEquals("Work not found", notFoundCategory.getMessage());
    }

    @Test
    void should_return_all_works() {
        Category category1 = getWork();
        Category category2 = getWork();
        service.createNewCategory(category1);
        service.createNewCategory(category2);
        List<Category> categories = service.getAllCategories();
        assertEquals(2, categories.size());

    }

    @Test
    void get_work_by_name() {
        Category category = getWork();
        Category newCategory = service.createNewCategory(category);
        Category categoryByName = service.getAllCategoriesByName(newCategory.getName());
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