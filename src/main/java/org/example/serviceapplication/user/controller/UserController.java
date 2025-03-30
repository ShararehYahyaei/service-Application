package org.example.serviceapplication.user.controller;


import jakarta.validation.Valid;
import org.example.serviceapplication.user.dto.*;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService
    ) {
        this.userService = userService;

    }

//    @PostMapping(value = "/create", consumes = "multipart/form-data")
//    public UserResponseDto save(
//            @ModelAttribute @Valid UserRequest userRequest
//    ) {
//        return userService.createUser(userRequest);
//    }

    @PostMapping(value = "/changePassword")
    public ResponseEntity updatePassword(
            @RequestBody PasswordUserRequest changePasswordRequest
    ) {
        userService.updatePassword(changePasswordRequest);
        return ResponseEntity.ok(HttpStatus.OK);
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

    @GetMapping("/getByIdForShowSubServiceCategory/{id}")
    public ResponseEntity<List<SpecialistWithSubService>> getUserWithSubServiceCategory(@PathVariable Long id) {
        List<SpecialistWithSubService> userWithSubServiceCategory = userService.getUserWithSubServiceCategory(id);
        return new ResponseEntity<>(userWithSubServiceCategory, HttpStatus.OK);
    }


    @PutMapping("/addCategoryToUser/{userId}/{subCategoryId}")
    public ResponseEntity addCategoryToUser(
            @PathVariable Long userId,
            @PathVariable Long subCategoryId) {
        userService.addSubCategory(userId, subCategoryId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/removeCategoryFromUser/{userId}/{subServiceCategory}")
    public ResponseEntity removeCategoryFromUser(
            @PathVariable Long userId,
            @PathVariable Long subServiceCategory) {

        userService.removeSubCategoryForSpecialist(userId, subServiceCategory);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/editSubServiceCategory/{userId}/{subServiceCategoryOld}/{subServiceCategoryNew}")
    public ResponseEntity editSubServiceCategory(
            @PathVariable Long userId,
            @PathVariable Long subServiceCategoryOld,
            @PathVariable Long subServiceCategoryNew) {
        userService.editSubServiceCategory(userId, subServiceCategoryOld, subServiceCategoryNew);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("getAllCustomers")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<CustomerResponseDto> allCustomers = userService.getAllCustomers();
        return new ResponseEntity<>(allCustomers, HttpStatus.OK);
    }

    @GetMapping("getAllSpecialist")
    public ResponseEntity<List<SpecialistResponseDto>> getAllSpecialists() {
        List<SpecialistResponseDto> allSpecialists = userService.getAllSpecialists();
        return new ResponseEntity<>(allSpecialists, HttpStatus.OK);

    }


}
