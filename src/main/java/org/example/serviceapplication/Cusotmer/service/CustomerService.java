package org.example.serviceapplication.Cusotmer.service;

import org.example.serviceapplication.Cusotmer.dto.CustomerCreateDto;
import org.example.serviceapplication.Cusotmer.dto.CustomerResponseDto;
import org.example.serviceapplication.Cusotmer.model.Customer;
import org.example.serviceapplication.Cusotmer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService implements CustomerServiceInterface {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Override
    public Customer createCustomer(CustomerCreateDto customerDto) {
        Customer customer = convertRequestDtoIntoEntity(customerDto);
        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponseDto findById(Long id) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.isPresent()) {
            return convertEntityToResponseDto(byId.get());
        }
        throw new RuntimeException("Customer not found");
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerResponseDto> getCustomersByName(String name) {
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);
        return customers.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerResponseDto> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    @Override
    public List<CustomerResponseDto> getAllCustomersWithActiveStatus() {
        List<Customer> allCustomers = customerRepository.findAll();
        return allCustomers.stream()
                .map(this::convertEntityToResponseDto)
                .collect(Collectors.toList());
    }


    private Customer convertRequestDtoIntoEntity(CustomerCreateDto customerCreateDto) {
        return new Customer(
                customerCreateDto.address(),
                customerCreateDto.phone(),
                customerCreateDto.name(),
                customerCreateDto.email(),
                customerCreateDto.password(),
                customerCreateDto.active(),
                customerCreateDto.role(),
                customerCreateDto.status(),
                customerCreateDto.profileImage()
        );
    }

    private CustomerResponseDto convertEntityToResponseDto(Customer customer) {
        return new CustomerResponseDto(
                customer.getAddress(),
                customer.getPhone(),
                customer.getName(),
                customer.isActive(),
                customer.getRole(),
                customer.getStatus()

        );
    }


}
