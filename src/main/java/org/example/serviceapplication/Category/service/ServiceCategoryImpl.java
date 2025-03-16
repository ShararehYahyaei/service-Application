package org.example.serviceapplication.Category.service;

import org.example.serviceapplication.Category.dto.ServiceCategoryRequest;
import org.example.serviceapplication.Category.dto.ServiceCategoryResponse;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryResponseDto;
import org.example.serviceapplication.Category.exception.DuplicateCategoryName;
import org.example.serviceapplication.Category.model.ServiceCategory;
import org.example.serviceapplication.Category.repository.ServiceCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service

public class ServiceCategoryImpl implements ServiceCategoryInterface {

    private final ServiceCategoryRepository serviceCategoryRepository;

    public ServiceCategoryImpl(ServiceCategoryRepository serviceCategoryRepository) {
        this.serviceCategoryRepository = serviceCategoryRepository;

    }

    @Transactional
    @Override
    public ServiceCategory createNewCategory(ServiceCategoryRequest serviceCategoryRequest) {
        ServiceCategory category = convertDtoToEntity(serviceCategoryRequest);
        if (serviceCategoryRepository.existsByName(category.getName())) {
            throw new DuplicateCategoryName("Duplicate category name");
        }
        return serviceCategoryRepository.save(category);

    }

    @Transactional(readOnly = true)
    @Override
    public ServiceCategoryResponse getCategoryByIdResponse(Long id) {
        Optional<ServiceCategory> categoryFoundById = serviceCategoryRepository.findById(id);
        if (categoryFoundById.isPresent()) {
            return convertEntityToResponseDto(categoryFoundById.get());
        }
        throw new RuntimeException("Category not found");

    }


    @Transactional(readOnly = true)
    @Override
    public ServiceCategory getCategoryById(Long id) {
        Optional<ServiceCategory> categoryFoundById = serviceCategoryRepository.findById(id);
        if (categoryFoundById.isPresent()) {
            return categoryFoundById.get();
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    @Transactional
    @Override
    public ServiceCategory updateCategory(ServiceCategory category) {
        Optional<ServiceCategory> categoryFound = serviceCategoryRepository.findById(category.getId());
        if (categoryFound.isEmpty()) {
            throw new RuntimeException("Category not found");
        }
        return serviceCategoryRepository.save(category);

    }

    @Transactional
    @Override
    public void deleteWork(long id) {
        Optional<ServiceCategory> categoryFound = serviceCategoryRepository.findById(id);
        if (categoryFound.isEmpty()) {
            throw new RuntimeException("Category not found");
        }
        serviceCategoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ServiceCategoryResponse> getAllCategories() {
        List<ServiceCategory> allCategories = serviceCategoryRepository.findAll();
        return allCategories.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ServiceCategoryResponse> getAllCategoriesByName(String name) {
        List<ServiceCategory> allCategoriesWithContainingName = serviceCategoryRepository.findByNameContainingIgnoreCase(name);
        if (allCategoriesWithContainingName.isEmpty()) {
            throw new RuntimeException("Category not found with name " + name);
        }
        return allCategoriesWithContainingName.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());

    }



    private ServiceCategory convertDtoToEntity(ServiceCategoryRequest serviceCategoryRequest) {
        return new ServiceCategory(
                serviceCategoryRequest.name()
        );
    }


    private ServiceCategoryResponse convertEntityToResponseDto(ServiceCategory category) {


        return new ServiceCategoryResponse(
                category.getName(),
                category.getSubServiceCategoryList().stream()
                        .map(subCategory -> new SubServiceCategoryResponseDto(
                                subCategory.getName(),
                                subCategory.getDescription(),
                                subCategory.getPrice()
                        ))
                        .collect(Collectors.toList())

        );
    }


}
