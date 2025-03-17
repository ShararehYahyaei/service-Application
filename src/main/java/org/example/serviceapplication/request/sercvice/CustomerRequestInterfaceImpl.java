package org.example.serviceapplication.request.sercvice;


import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.request.enumPackage.RequestStatus;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.request.repository.CustomerRequestRepo;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.dto.UserResponseDto;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class CustomerRequestInterfaceImpl implements CustomerRequestInterface {

    private final CustomerRequestRepo requestRepo;
    private final UserService userRepo;
    private final SubServiceCategoryInterface subService;

    public CustomerRequestInterfaceImpl(CustomerRequestRepo requestRepo
            , UserService userRepo,
                                        SubServiceCategoryInterface subService) {
        this.requestRepo = requestRepo;
        this.userRepo = userRepo;
        this.subService = subService;
    }

    @Transactional
    @Override
    public void addRequest(CustomerRequestDto customerRequest) {

        CustomerRequest request = convertRequestIntoEntity(customerRequest);
        request.setRequestDate(LocalDateTime.now());
        request.setRequestStatus(RequestStatus.AwaitingSpecialistOffers);
        requestRepo.save(request);
    }


    //todo create convert to entity method

    private CustomerRequest convertRequestIntoEntity(CustomerRequestDto customerRequestDto) {
        User userForRequest = userRepo.getUserById(customerRequestDto.userId());
        SubServiceCategory subServiceCategoryById = subService.getSubServiceCategoryById(customerRequestDto.subServiceCategory());
        double price = subServiceCategoryById.getPrice();
        if (price <= customerRequestDto.price()) {
            //todo begam price bishtar az price asli hast
        }

        return new CustomerRequest(
                userForRequest,
                subServiceCategoryById,
                customerRequestDto.price(),
                customerRequestDto.deadLineTime(),
                customerRequestDto.description(),
                customerRequestDto.address()

        );
    }


}
