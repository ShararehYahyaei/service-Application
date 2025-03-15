package org.example.serviceapplication.Category.controller;

import org.example.serviceapplication.Category.dto.CategoryRequest;
import org.example.serviceapplication.Category.dto.CategoryResponse;
import org.example.serviceapplication.Category.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping("/create")
    public void createCategory(@RequestBody CategoryRequest categoryRequest) {
        categoryService.createNewCategory(categoryRequest);

    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> allCategories = categoryService.getAllCategories();
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }


}
