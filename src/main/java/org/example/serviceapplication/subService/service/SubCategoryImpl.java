package org.example.serviceapplication.subService.service;

import org.example.serviceapplication.subService.model.SubCategory;
import org.example.serviceapplication.subService.repsitory.SubCategoryRepository;
import org.example.serviceapplication.subService.subCategoryDto.SubCategoryCreateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SubCategoryImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;

    public SubCategoryImpl(SubCategoryRepository subCategoryRepository) {
        this.subCategoryRepository = subCategoryRepository;
    }


    @Transactional
    @Override
    public SubCategory createSubCategory(SubCategoryCreateDto subCategoryCreateDto) {
        SubCategory subCategory = convertSubCategoryDtoToSubCAtegory(subCategoryCreateDto);
        return subCategoryRepository.save(subCategory);

    }







    private SubCategory convertSubCategoryDtoToSubCAtegory(SubCategoryCreateDto subCategoryCreateDto) {
        return new SubCategory(
                subCategoryCreateDto.name(),
                subCategoryCreateDto.description(),
                subCategoryCreateDto.price()

        );
    }


}
