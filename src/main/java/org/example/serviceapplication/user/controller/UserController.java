package org.example.serviceapplication.user.controller;


import org.example.serviceapplication.user.dto.*;
import org.example.serviceapplication.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public UserResponseDto  save(
            @ModelAttribute UserRequest userRequest
    ) {
        return userService.createUser(userRequest);
    }


    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> getAllUsers = userService.getAllUsers();
        return new ResponseEntity<>(getAllUsers, HttpStatus.OK);
    }

    @GetMapping("/getActiveUsers")
    public ResponseEntity<List<UserResponseDto>> getActiveUsers() {
        List<UserResponseDto> getAllUsers = userService.getAllActiveUsers();
        return new ResponseEntity<>(getAllUsers, HttpStatus.OK);
    }

    @GetMapping("/getUsersByName")
    public ResponseEntity<List<UserResponseDto>> getUsersByName(@RequestParam String name) {
        List<UserResponseDto> allUsers = userService.getUsersByName(name);
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/getById/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/getByIdForShowCategory/{id}")
    public UserResponseWithSubCategory getUserByIdForShowCategory(@PathVariable Long id) {
        return userService.getUserWithCategory(id);
    }


    @PutMapping("/addCategoryToUser/{userId}/{categoryId}")
    public ResponseEntity<UserResponseWithSubCategory> addCategoryToUser(
            @PathVariable Long userId,
            @PathVariable Long categoryId) {

        UserResponseWithSubCategory userResponseWithSubCategory = userService.addCategoryToSpecialist(userId, categoryId);
        return ResponseEntity.ok(userResponseWithSubCategory);
    }

    @PutMapping("/removeCategoryFromUser/{userId}/{categoryId}")
    public ResponseEntity<UserResponseWithoutSubCategory> removeCategoryFromUser(
            @PathVariable Long userId,
            @PathVariable Long categoryId) {

        UserResponseWithoutSubCategory user = userService.deleteCategoryFromSpecialist(userId, categoryId);
        return ResponseEntity.ok(user);
    }


}
