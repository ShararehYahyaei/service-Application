package org.example.serviceapplication.user.controller;


import jakarta.validation.Valid;
import org.example.serviceapplication.user.dto.UserRequest;
import org.example.serviceapplication.user.dto.UserResponse;
import org.example.serviceapplication.user.dto.UserResponseWithCategory;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public UserResponse save(
            @ModelAttribute UserRequest userRequest
    ) {
        return userService.userCreate(userRequest);
    }


    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> getAllUsers = userService.getAllUsers();
        return new ResponseEntity<>(getAllUsers, HttpStatus.OK);
    }

    @GetMapping("/getActiveUsers")
    public ResponseEntity<List<UserResponse>> getActiveUsers() {
        List<UserResponse> getAllUsers = userService.getAllActiveUsers();
        return new ResponseEntity<>(getAllUsers, HttpStatus.OK);
    }

    @GetMapping("/getUsersByName")
    public ResponseEntity<List<UserResponse>> getUsersByName(@RequestParam String name) {
        List<UserResponse> allUsers = userService.getUsersByName(name);
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }


    @PutMapping("/addCategoryToUser/{userId}/{categoryId}")
    public ResponseEntity<UserResponseWithCategory> addCategoryToUser( @RequestParam Long userId,
                                                                      @RequestParam Long categoryId  ) {
        UserResponseWithCategory userResponseWithCategory = userService.addCategoryToSpecialist(userId, categoryId);
        return new ResponseEntity<>(userResponseWithCategory, HttpStatus.OK);
    }



}
