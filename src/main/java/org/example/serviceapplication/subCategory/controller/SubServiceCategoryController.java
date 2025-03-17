package org.example.serviceapplication.subCategory.controller;


import org.example.serviceapplication.subCategory.dto.UpdateSubServiceCategory;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryRequest;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subCategory")
public class SubServiceCategoryController {

    private final SubServiceCategoryInterface subServiceCategoryInterface;

    public SubServiceCategoryController(SubServiceCategoryInterface subServiceCategoryInterface) {
        this.subServiceCategoryInterface = subServiceCategoryInterface;
    }

    @PostMapping("/create")
    public SubServiceCategoryResponse createSubcategory(@RequestBody SubServiceCategoryRequest subServiceCategoryRequest) {
        return subServiceCategoryInterface.createSubServiceCategory(subServiceCategoryRequest);

    }


    @GetMapping("/below-price")
    public ResponseEntity<List<SubServiceCategoryResponse>> getAllSubCategoriesLessThanThisPrice(@RequestParam double price) {
        List<SubServiceCategoryResponse> subCategories = subServiceCategoryInterface.
                getSubCategoriesLessThanThisPrice(price);
        return new ResponseEntity<>(subCategories, HttpStatus.OK);
    }

    @PatchMapping  ("editSubServiceCategory/{id}")
    public ResponseEntity updateSubCategory(@PathVariable Long id,
                                            @RequestBody UpdateSubServiceCategory updateSubServiceCategory) {
        subServiceCategoryInterface.editSubServiceCategory(id,updateSubServiceCategory);
        return new ResponseEntity(HttpStatus.OK);

    }
    @DeleteMapping("deleteSubServiceCategory/{id}")
    public ResponseEntity deleteSubCategory(@PathVariable Long id) {
        subServiceCategoryInterface.deleteSubServiceCategory(id);
        return new ResponseEntity(HttpStatus.OK);
    }

}


