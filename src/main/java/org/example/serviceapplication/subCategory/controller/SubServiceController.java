package org.example.serviceapplication.subCategory.controller;


import org.example.serviceapplication.subCategory.service.SubCategoryService;
import org.example.serviceapplication.subCategory.dto.SubCategoryRequest;
import org.example.serviceapplication.subCategory.dto.SubCategoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subCategory")
public class SubServiceController {

    private final SubCategoryService subCategoryService;

    public SubServiceController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PostMapping("/create")
    public void createSubcategory(@RequestBody SubCategoryRequest subCategoryRequest) {
        subCategoryService.createSubCategory(subCategoryRequest);

    }


    @GetMapping("/below-price")
    public ResponseEntity<List<SubCategoryResponse>> getAllSubCategoriesLessThanThisPrice(@RequestParam double price) {
        List<SubCategoryResponse> subCategories = subCategoryService.
                getSubCategoriesLessThanThisPrice(price);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

}
