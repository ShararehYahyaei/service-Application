package org.example.serviceapplication.user.webController.AdminController;

import jakarta.validation.Valid;
import org.example.serviceapplication.Category.dto.ServiceCategoryRequest;
import org.example.serviceapplication.Category.dto.ServiceCategoryResponse;
import org.example.serviceapplication.Category.exception.NotFoundCategory;
import org.example.serviceapplication.Category.service.ServiceCategoryInterface;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryRequest;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.dto.CustomerResponseDto;
import org.example.serviceapplication.user.dto.SpecialistResponseDto;
import org.example.serviceapplication.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminWebController {

    private final UserService userService;
    private final ServiceCategoryInterface categoryService;
    private final SubServiceCategoryInterface subServiceCategoryInterface;


    public AdminWebController(UserService userService,
                              ServiceCategoryInterface categoryService, SubServiceCategoryInterface subServiceCategoryInterface) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.subServiceCategoryInterface = subServiceCategoryInterface;
    }

    //todo show all customers
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

    //todo show all specialists

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

    //todo show all categories

    @GetMapping("/categoriesListPage")
    public String showCategoriesList(Model model) {

        List<ServiceCategoryResponse> allCategories = categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("showList", true);

        return "categoriesList";
    }


    //todo add subService category
    @GetMapping("/addServiceCategory")
    public String addServiceCategoryForm(Model model) {
        model.addAttribute("serviceCategoryRequest", new ServiceCategoryRequest(""));
        return "addServiceCategory";
    }

    //todo add category Service to show page
    @PostMapping("/createCategory")
    public String createCategory(@ModelAttribute ServiceCategoryRequest serviceCategoryRequest) {
        categoryService.createNewCategory(serviceCategoryRequest);
        return "redirect:/servicesList";
    }


    //todo add sub ServiceCategory to each category in the sub Service age
    @GetMapping("/add-sub-service/{categoryKey}")
    public String showAddSubServiceForm(@PathVariable String categoryKey, Model model) {
        Long categoryId = categoryService.findIdByName(categoryKey)
                .orElseThrow(() -> new NotFoundCategory("Category Not Found "));
        SubServiceCategoryRequest subServiceRequest = new SubServiceCategoryRequest(
                null, "", "", 0.0, categoryId);
        model.addAttribute("subServiceRequest", subServiceRequest);
        return "add-sub-service";
    }

    //todo add sub ServiceCategory to each category in the category list page
    @GetMapping("/add-sub-service/by-id/{categoryId}")
    public String addSubService(@PathVariable Long categoryId, Model model) {
        SubServiceCategoryRequest subServiceRequest = new SubServiceCategoryRequest(
                null, "", "", 0.0, categoryId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("subServiceRequest", subServiceRequest);
        return "add-sub-service";
    }


    //todo add new sub service category
    @PostMapping("/save-sub-service")
    public String saveSubService(@ModelAttribute @Valid SubServiceCategoryRequest subServiceRequest) {
        subServiceCategoryInterface.createSubServiceCategory(subServiceRequest);
        return "redirect:/servicesList";
    }


    @GetMapping("/get-Profile-Admin")
    public String getProfileAdmin(Model model) {
        return " get-Profile-Admin";
    }

}
