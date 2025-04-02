package org.example.serviceapplication.subCategory.service;

import org.example.serviceapplication.Category.model.ServiceCategory;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.dto.UpdateSubServiceCategory;
import org.example.serviceapplication.subCategory.exception.SubServiceCategoryIsNotFound;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.subCategory.repsitory.SubServiceCategoryRepository;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryRequest;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryResponse;
import org.example.serviceapplication.Category.service.ServiceCategoryInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class SubServiceCategoryImpl implements SubServiceCategoryInterface {
    private final SubServiceCategoryRepository subServiceCategoryRepository;
    private final ServiceCategoryInterface serviceCategoryInterface;
    private final Logger logger = LoggerFactory.getLogger(SubServiceCategoryImpl.class);

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


    @Transactional(readOnly = true)
    @Override
    public SubServiceCategory getSubServiceCategoryById(long id) {
        Optional<SubServiceCategory> byId = subServiceCategoryRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        logger.error("SubServiceCategory with id {} not found", id);
        throw new SubServiceCategoryIsNotFound("Sub Service Category Not Found");
    }

    @Transactional
    @Override
    public void editSubServiceCategory(Long id, UpdateSubServiceCategory updateSubServiceCategory) {
        Optional<SubServiceCategory> foundSubService = subServiceCategoryRepository.findById(id);
        if (foundSubService.isEmpty()) {
            logger.error("SubServiceCategory with id {} not found", id);
            throw new SubServiceCategoryIsNotFound("SubServiceCategory not found with ID: " + id);
        }
        if (updateSubServiceCategory.name() != null) {
            foundSubService.get().setName(updateSubServiceCategory.name());
        }
        if (updateSubServiceCategory.description() != null) {
            foundSubService.get().setDescription(updateSubServiceCategory.description());
        }
        if (updateSubServiceCategory.price() != null) {
            foundSubService.get().setPrice(updateSubServiceCategory.price());
        }

        subServiceCategoryRepository.save(foundSubService.get());
    }

    @Transactional
    @Override
    public void deleteSubServiceCategory(Long id) {
        Optional<SubServiceCategory> found = subServiceCategoryRepository.findById(id);
        logger.info("SubServiceCategory with id {} found", id);
        if (found.isPresent()) {
            subServiceCategoryRepository.delete(found.get());
        } else {
            throw new SubServiceCategoryIsNotFound("Sub Service Category Not Found");
        }

    }

    @Override
    public List<SubServiceCategories> getAllSubServiceCatgories() {
        List<SubServiceCategory> all = subServiceCategoryRepository.findAll();
        return convertSubCategoryToRes(all);

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

    private List<SubServiceCategories> convertSubCategoryToRes(List<SubServiceCategory> subServiceCategory) {
        return subServiceCategory.stream()
                .map(sub -> new SubServiceCategories(
                        sub.getId(),
                        sub.getName(),
                        sub.getDescription(),
                        sub.getPrice(),
                        sub.getCategory().getName()
                ))
                .collect(Collectors.toList());
    }


}
