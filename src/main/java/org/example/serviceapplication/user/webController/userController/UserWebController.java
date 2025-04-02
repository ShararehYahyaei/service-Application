package org.example.serviceapplication.user.webController.userController;

import jakarta.validation.Valid;
import org.example.serviceapplication.Category.model.ServiceCategory;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryRequest;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryResponse;
import org.example.serviceapplication.subCategory.dto.SubServiceDto;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.dto.SpecialistResponseDto;
import org.example.serviceapplication.user.dto.UserRequest;
import org.example.serviceapplication.user.dto.UserResponseDto;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.exception.UserNotFond;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserWebController {
    private final UserService userService;
    private final SubServiceCategoryInterface subservice;
    private final Logger logger = LoggerFactory.getLogger(UserWebController.class);

    public UserWebController(UserService userService, SubServiceCategoryInterface subservice) {
        this.userService = userService;
        this.subservice = subservice;

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
        model.addAttribute("user", user);
        return "redirect:/servicesList";
    }

    public List<SubServiceDto> convertToSubServiceDtoList(List<SubServiceCategories> allSubServices) {
        return allSubServices.stream()
                .map(sub -> new SubServiceDto(sub.id(), sub.name()))
                .collect(Collectors.toList());
    }

    @GetMapping("/specialist-profile")
    public String getSpecialistProfile(@RequestParam(value = "specialistId", required = false) Long specialistId, Model model) {
        if (specialistId != null) {
            try {
                User specialist = userService.getUserById(specialistId);

                if (specialist == null) {
                    model.addAttribute("error", "متخصص یافت نشد.");
                } else {
                    model.addAttribute("specialist", specialist);
                }
            } catch (Exception e) {
                model.addAttribute("error", "خطا در دریافت اطلاعات متخصص.");
            }
        }
        return "specialist-profile";
    }


    @GetMapping("/searchUsers")
    public String searchUsers(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "email", required = false) String email,
                              @RequestParam(value = "role", required = false) String role,
                              Model model) {

        List<User> users = userService.searchUsers(name, email, role);
        model.addAttribute("users", users);
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("role", role);

        return "searchUsers";
    }


}
