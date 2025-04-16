package org.example.serviceapplication.user.controller;

import jakarta.validation.Valid;
import org.example.serviceapplication.credit.exception.CreditNotFoundException;
import org.example.serviceapplication.credit.model.Credit;
import org.example.serviceapplication.credit.service.CreditService;
import org.example.serviceapplication.review.model.ReviewDto;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.dto.SubServiceDto;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.dto.UserRequest;
import org.example.serviceapplication.user.dto.UserResponseDto;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.exception.UserHasWrongRole;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.UserService;
import org.example.serviceapplication.verification.service.VerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserWebController {
    private final UserService userService;
    private final SubServiceCategoryInterface subservice;
    private final CreditService creditService;
    private final VerificationService verificationService;
    private final Logger logger = LoggerFactory.getLogger(UserWebController.class);

    public UserWebController(UserService userService, SubServiceCategoryInterface subservice, CreditService creditService, VerificationService verificationService) {
        this.userService = userService;
        this.subservice = subservice;

        this.creditService = creditService;
        this.verificationService = verificationService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        List<SubServiceCategories> allSubServices = subservice.getAllSubServiceCatgories();
        List<SubServiceDto> subServiceDtos = convertToSubServiceDtoList(allSubServices);
        model.addAttribute("subServiceDtos", subServiceDtos);
        return "register";
    }

        @PostMapping(value = "/create", consumes = "multipart/form-data")
    public String createUser(@ModelAttribute @Valid UserRequest userRequest,
                             @RequestParam(required = false) MultipartFile profileImage,
                             BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "error";
        }


        UserResponseDto user = userService.createUser(userRequest, profileImage);

        String token = verificationService.generateVerificationToken(user.getEmail());

        verificationService.sendVerificationEmail(user.getEmail(), token);

        model.addAttribute("user", user);
        return "registration-success";
    }


    @GetMapping("/verify")
    public String verifyEmail(@RequestParam("token") String token) {
        boolean isValid = verificationService.verifyToken(token);
        if (isValid) {
            userService.activateUser(token);
            return "email-confirmation-success";

        } else {
            return "email-confirmation-error";
        }
    }


    public List<SubServiceDto> convertToSubServiceDtoList(List<SubServiceCategories> allSubServices) {
        return allSubServices.stream()
                .map(sub -> new SubServiceDto(sub.id(), sub.name()))
                .collect(Collectors.toList());
    }

    @GetMapping("/getProfile")
    public String getSpecialistProfile() {
        return "getProfile";
    }


    @GetMapping("/specialist-profile")
    public String getSpecialistProfile(@RequestParam("specialistId") Long specialistId, Model model) {
        User specialist = userService.getUserById(specialistId);

        if (specialist == null) {
            model.addAttribute("error", "متخصصی با این شناسه یافت نشد.");

        }

        model.addAttribute("specialist", specialist);
        model.addAttribute("reviewDto", new ReviewDto(specialistId,
                null, null, 0, null));
        return "get-Specialist-profile";
    }


    @GetMapping("/searchUsers")
    public String searchUsers(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "email", required = false) String email,
                              @RequestParam(value = "role", required = false) String role,
                              Model model) {

        List<User> users = userService.searchUsers(name, email, role);
        model.addAttribute("users", users);
        return "searchUsers";
    }


    @GetMapping("/getCustomerId")
    public String showCustomerIdForm( Model model) {
        return "getCustomerId";
    }


    @GetMapping("/view-my-credit")
    public String getSpecialistCredit(@RequestParam(value = "userIdCredit", required = false) Long customerId,
                                      @RequestParam(value = "Role") Role role, Model model) {
        User specialist = userService.getUserById(customerId);

        if (specialist.getRole() == Role.Admin) {
            throw new UserHasWrongRole("User has wrong role");
        }

        Optional<Credit> credit = creditService.getCreditByUserId(customerId);
        if (credit.isEmpty()) {
            throw new CreditNotFoundException("credit not found");
        }

        model.addAttribute("credit", credit.get());
        model.addAttribute("Role", role);

        return "view-my-credit.html";
    }


    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "نام کاربری یا رمز عبور اشتباه است!");
        }
        return "login";
    }


}
