package org.example.serviceapplication.subService.controller;



import org.example.serviceapplication.subService.service.SubCategoryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/subService")
public class SubServiceController {

    private final SubCategoryService subCategoryService;

    public SubServiceController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PostMapping
    public void createSubcategory(@RequestBody SubCategoryRequest subCategoryRequest) {

    }
}
