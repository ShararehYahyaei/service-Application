package org.example.serviceapplication.subCategory.service;

import org.example.serviceapplication.Category.dto.ServiceCategoryRequest;
import org.example.serviceapplication.Category.dto.ServiceCategoryResponse;
import org.example.serviceapplication.Category.model.ServiceCategory;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.subCategory.repsitory.SubServiceCategoryRepository;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryRequest;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryResponse;
import org.example.serviceapplication.Category.service.ServiceCategoryInterface;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SubServiceCategoryImpl implements SubServiceCategoryInterface {
    private final SubServiceCategoryRepository subServiceCategoryRepository;
    private final ServiceCategoryInterface serviceCategoryInterface;

    public SubServiceCategoryImpl(SubServiceCategoryRepository subServiceCategoryRepository,
                                  ServiceCategoryInterface serviceCategoryInterface) {
        this.subServiceCategoryRepository = subServiceCategoryRepository;
        this.serviceCategoryInterface = serviceCategoryInterface;
    }

    @Transactional
    @Override
    public SubServiceCategoryResponse createSubServiceCategory(SubServiceCategoryRequest subServiceCategoryRequest) {

        ServiceCategory serviceCategory = serviceCategoryInterface.getCategoryById(subServiceCategoryRequest.categoryId());
        serviceCategory.getSubServiceCategoryList().add(
                new SubServiceCategory(
                subServiceCategoryRequest.name(),
                subServiceCategoryRequest.description(),
                subServiceCategoryRequest.price(),
                serviceCategory
        ));

        serviceCategoryInterface.updateCategory(serviceCategory);
        return convertSubServiceCategoryRequestToServiceCategory(subServiceCategoryRequest);

    }


    @Transactional(readOnly = true)
    @Override
    public List<SubServiceCategoryResponse> getSubCategoriesLessThanThisPrice(double price) {
        List<SubServiceCategory> subCategories = subServiceCategoryRepository.findByPriceLessThan(price);
        return subCategories.stream()
                .map(this::convertSubCategoryToResponse)
                .collect(Collectors.toList());
    }


    private SubServiceCategoryResponse convertSubServiceCategoryRequestToServiceCategory
            (SubServiceCategoryRequest subServiceCategoryRequest) {
        ServiceCategory categoryFound = serviceCategoryInterface.getCategoryById(subServiceCategoryRequest.categoryId());
        return new SubServiceCategoryResponse(
                subServiceCategoryRequest.name(),
                subServiceCategoryRequest.description(),
                subServiceCategoryRequest.price(),
                categoryFound.getName()
        );
    }


    private SubServiceCategoryResponse convertSubCategoryToResponse
            (SubServiceCategory subServiceCategory) {
        return new SubServiceCategoryResponse(
                subServiceCategory.getName(),
                subServiceCategory.getDescription(),
                subServiceCategory.getPrice(),
                subServiceCategory.getCategory().getName()

        );
    }



}
