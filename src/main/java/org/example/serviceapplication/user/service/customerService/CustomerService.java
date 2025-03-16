package org.example.serviceapplication.user.service.customerService;

import org.example.serviceapplication.user.dto.CustomerResponseDto;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.userRepository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final UserRepository userRepository;
    public CustomerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CustomerResponseDto createCustomer(User user) {
        User customer = userRepository.save(user);
        return convertEntityToResponseDto(customer);

    }


    public CustomerResponseDto convertEntityToResponseDto(User user) {
        return new CustomerResponseDto(
                user.getAddress(),
                user.getPhone(),
                user.getName(),
                user.isActive(),
                user.getRole(),
                user.getStatus()

        );
    }


}
