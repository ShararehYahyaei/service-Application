package org.example.serviceapplication.user.webController.AdminController;

import org.example.serviceapplication.Category.dto.ServiceCategoryRequest;
import org.example.serviceapplication.Category.dto.ServiceCategoryResponse;
import org.example.serviceapplication.Category.service.ServiceCategoryInterface;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.user.dto.CustomerResponseDto;
import org.example.serviceapplication.user.dto.SpecialistResponseDto;
import org.example.serviceapplication.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AdminWebController {

    private final UserService userService;
    private final ServiceCategoryInterface categoryService;


    public AdminWebController(UserService userService,
                              ServiceCategoryInterface categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }


    @GetMapping("/all-customers")
    public String getAllCustomers(Model model) {
        String role = "ADMIN";

        if (!"ADMIN".equals(role)) {
            return "access-denied";
        }
        List<CustomerResponseDto> allCustomers = userService.getAllCustomers();
        model.addAttribute("customers", allCustomers);
        return "customer-list";
    }

    @GetMapping("/all-specialists")
    public String getAllSpecialists(Model model) {
        String role = "ADMIN";
        if (!"ADMIN".equals(role)) {
            return "access-denied";
        }
        List<SpecialistResponseDto> allSpecialists = userService.getAllSpecialists();
        model.addAttribute("specialists", allSpecialists);
        return "specialist-list";
    }

    @GetMapping("/categoriesListPage")
    public String showCategoriesList(Model model) {

        List<ServiceCategoryResponse> allCategories = categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("showList", true);
        return "categoriesList";
    }


    @GetMapping("/addServiceCategory")
    public String addServiceCategoryForm(Model model) {
        model.addAttribute("serviceCategoryRequest", new ServiceCategoryRequest(""));
        return "addServiceCategory";
    }

    @PostMapping("/createCategory")
    public String createCategory(@ModelAttribute ServiceCategoryRequest serviceCategoryRequest) {
        categoryService.createNewCategory(serviceCategoryRequest);
        return "redirect:/servicesList";
    }





}
