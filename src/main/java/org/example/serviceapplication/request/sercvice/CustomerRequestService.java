package org.example.serviceapplication.request.sercvice;

import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.request.dto.CustomerRequestResponseDto;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.user.model.User;

import java.util.List;

public interface CustomerRequestService {
    void addRequest(User customer, CustomerRequestDto customerRequest);
    CustomerRequest findRequestById(Long id);
    List<CustomerRequestResponseDto> getAllRequestForSpecialist(List<SubServiceCategory>subServices);
    List<CustomerRequestResponseDto> getAllCustomerRequestDtoForCustomer(Long userId);
}
