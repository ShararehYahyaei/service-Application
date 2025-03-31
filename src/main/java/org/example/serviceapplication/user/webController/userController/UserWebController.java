package org.example.serviceapplication.user.webController.userController;

import jakarta.validation.Valid;
import org.example.serviceapplication.Category.model.ServiceCategory;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryRequest;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryResponse;
import org.example.serviceapplication.subCategory.dto.SubServiceDto;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.dto.UserRequest;
import org.example.serviceapplication.user.dto.UserResponseDto;
import org.example.serviceapplication.user.service.UserService;
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
    private final ConversionService conversionService;

    public UserWebController(UserService userService, SubServiceCategoryInterface subservice, ConversionService conversionService) {
        this.userService = userService;
        this.subservice = subservice;
        this.conversionService = conversionService;
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
            return "register";
        }

        try {

            UserResponseDto user = userService.createUser(userRequest, profileImage);
            model.addAttribute("user", user);
            return "redirect:/servicesList";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    public List<SubServiceDto> convertToSubServiceDtoList(List<SubServiceCategories> allSubServices) {
        return allSubServices.stream()
                .map(sub -> new SubServiceDto(sub.id(), sub.name()))
                .collect(Collectors.toList());
    }


}
