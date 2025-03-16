package org.example.serviceapplication.user.service;

import org.example.serviceapplication.user.dto.UserRequest;
import org.example.serviceapplication.user.dto.UserResponse;
import org.example.serviceapplication.user.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {


    @Transactional
    UserResponse userCreate(UserRequest userRequest);

    @Transactional(readOnly = true)
    UserResponse findById(Long id);

    @Transactional(readOnly = true)
    List<UserResponse> getUsersByName(String name);

    @Transactional(readOnly = true)
    List<UserResponse> getAllUsers();

    @Transactional(readOnly = true)
    List<UserResponse> getAllActiveUsers();
}
