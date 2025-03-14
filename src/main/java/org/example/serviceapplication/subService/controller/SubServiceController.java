package org.example.serviceapplication.subService.controller;


import org.example.serviceapplication.Cusotmer.dto.CustomerResponseDto;
import org.example.serviceapplication.subService.service.SubCategoryService;
import org.example.serviceapplication.subService.subCategoryDto.SubCategoryCreateDto;
import org.example.serviceapplication.subService.subCategoryDto.SubCategoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/v1/subCategory")
public class SubServiceController {

    private final SubCategoryService subCategoryService;

    public SubServiceController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @PostMapping
    public void createSubcategory(@RequestBody SubCategoryCreateDto subCategoryCreateDto) {

        subCategoryService.createSubCategory(subCategoryCreateDto);

    }


    @GetMapping("/services/below-price")
    public ResponseEntity<List<SubCategoryResponse>> getAllSubCategoriesLessThanThisPrice(@RequestParam double price) {
        List<SubCategoryResponse> subCategories = subCategoryService.
                getSubCategoriesLessThanThisPrice(price);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

}
