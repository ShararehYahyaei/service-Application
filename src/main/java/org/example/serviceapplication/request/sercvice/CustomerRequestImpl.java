package org.example.serviceapplication.request.sercvice;


import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.request.dto.CustomerRequestResponseDto;
import org.example.serviceapplication.request.exception.RequestStatusIsNotCorrect;
import org.example.serviceapplication.request.model.RequestStatus;
import org.example.serviceapplication.request.exception.RequestNotPresent;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.request.repository.CustomerRequestRepo;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CustomerRequestImpl implements CustomerRequestService {

    private final Logger logger = LoggerFactory.getLogger(CustomerRequestImpl.class);
    private final CustomerRequestRepo requestRepo;

    private final SubServiceCategoryInterface subService;

    public CustomerRequestImpl(CustomerRequestRepo requestRepo,
                               SubServiceCategoryInterface subService) {
        this.requestRepo = requestRepo;
        this.subService = subService;
    }

    @Transactional
    @Override
    public CustomerRequest addRequest(User customer, CustomerRequestDto customerRequest) {
        CustomerRequest request = convertRequestIntoEntity(customer, customerRequest);
        request.setRequestDate(LocalDateTime.now());
        request.setRequestStatus(RequestStatus.AwaitingOffers);
        requestRepo.save(request);
        return request;
    }

    @Override
    public CustomerRequest findRequestById(Long id) {
        Optional<CustomerRequest> request = requestRepo.findById(id);
        if (request.isPresent()) {
            return request.get();
        }
        throw new RequestNotPresent("RequestNotPresent");
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerRequestResponseDto> getAllRequestForSpecialist(List<SubServiceCategory> subServices) {
        List<CustomerRequest> allBySubServiceCategoryIn = requestRepo.findAllBySubServiceCategoryIn(subServices);
        if (allBySubServiceCategoryIn.isEmpty()) {
            logger.error("No requests found");
            throw new RequestNotPresent("RequestNotPresent");
        }

        return convertEntityToResponse(allBySubServiceCategoryIn);
    }

    @Override
    public List<CustomerRequestResponseDto> getAllCustomerRequestDtoForCustomer(Long userId) {
        List<CustomerRequest> requestForCustomer = requestRepo.findByUserId(userId);
        if (requestForCustomer.isEmpty()) {
            logger.error("No requests found");
            throw new RequestNotPresent("RequestNotPresent");
        }
        return convertEntityToResponse(requestForCustomer);
    }


    private CustomerRequest convertRequestIntoEntity(User customer, CustomerRequestDto customerRequestDto) {
        SubServiceCategory subServiceCategoryById = subService.
                getSubServiceCategoryById(customerRequestDto.subServiceCategory());
        return new CustomerRequest(
                customer,
                subServiceCategoryById,
                customerRequestDto.price(),
                customerRequestDto.deadLineTime(),
                customerRequestDto.description(),
                customerRequestDto.address()

        );
    }


    private List<CustomerRequestResponseDto> convertEntityToResponse(List<CustomerRequest> newRequests) {
        List<CustomerRequestResponseDto> filteredRequests = newRequests.stream()
                .filter(request -> !request.getRequestStatus().equals(RequestStatus.InProgress) )
                .map(request -> new CustomerRequestResponseDto(
                        request.getId(),
                        request.getRequestPrice(),
                        request.getDescription(),
                        request.getDeadLineTime(),
                        request.getAddress(),
                        request.getRequestStatus()
                ))
                .collect(Collectors.toList());

        if (filteredRequests.isEmpty()) {
            throw new RequestStatusIsNotCorrect("No requests with status AwaitingOffers were found");
        }

        return filteredRequests;
    }

    @Transactional
    @Override
    public void updateRequest(CustomerRequest customerRequest) {
        requestRepo.save(customerRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public Long countAllRequests() {
        return requestRepo.count();
    }

}
