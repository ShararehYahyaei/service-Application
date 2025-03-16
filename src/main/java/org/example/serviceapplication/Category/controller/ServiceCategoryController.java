package org.example.serviceapplication.Category.controller;

import org.example.serviceapplication.Category.dto.ServiceCategoryRequest;
import org.example.serviceapplication.Category.dto.ServiceCategoryResponse;
import org.example.serviceapplication.Category.service.ServiceCategoryInterface;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class ServiceCategoryController {

    private final ServiceCategoryInterface categoryService;

    public ServiceCategoryController(ServiceCategoryInterface categoryService) {
        this.categoryService = categoryService;
    }


    @PostMapping("/create")
    public void createCategory(@RequestBody ServiceCategoryRequest serviceCategoryRequest) {
        categoryService.createNewCategory(serviceCategoryRequest);

    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<List<ServiceCategoryResponse>> getAllCategories() {
        List<ServiceCategoryResponse> allCategories = categoryService.getAllCategories();
        return new ResponseEntity<>(allCategories, HttpStatus.OK);
    }


}
