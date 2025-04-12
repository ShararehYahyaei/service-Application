package org.example.serviceapplication.user.controller.admin;

import jakarta.validation.Valid;
import org.example.serviceapplication.Category.dto.ServiceCategoryRequest;
import org.example.serviceapplication.Category.dto.ServiceCategoryResponse;
import org.example.serviceapplication.Category.exception.NotFoundCategory;
import org.example.serviceapplication.Category.service.ServiceCategoryInterface;
import org.example.serviceapplication.order.model.OrderDto;
import org.example.serviceapplication.order.model.OrderDtoSearch;
import org.example.serviceapplication.order.service.OrderService;
import org.example.serviceapplication.request.sercvice.CustomerRequestService;
import org.example.serviceapplication.subCategory.dto.SubServiceCategoryRequest;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.dto.CustomerResponseDto;
import org.example.serviceapplication.user.dto.SpecialistResponseDto;
import org.example.serviceapplication.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
public class AdminWebController {

    private final UserService userService;
    private final ServiceCategoryInterface categoryService;
    private final SubServiceCategoryInterface subServiceCategoryInterface;
    private final OrderService orderService;
    private final CustomerRequestService customerRequestService;


    public AdminWebController(UserService userService,
                              ServiceCategoryInterface categoryService, SubServiceCategoryInterface subServiceCategoryInterface, OrderService orderService, CustomerRequestService customerRequestService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.subServiceCategoryInterface = subServiceCategoryInterface;
        this.orderService = orderService;
        this.customerRequestService = customerRequestService;
    }

    @GetMapping("/all-customers")
    public String getAllCustomers(
            @RequestParam(name = "fromLocalDate", required = false) LocalDate fromLocalDate,
            @RequestParam(name = "toLocalDate", required = false) LocalDate toLocalDate,

            Model model) {
        String role = "ADMIN";

        if (!"ADMIN".equals(role)) {
            return "access-denied";
        }
        List<CustomerResponseDto> allCustomers = userService.getAllCustomers();

        allCustomers = filterCustomerDtoByDate(fromLocalDate, toLocalDate, allCustomers);


        model.addAttribute("customers", allCustomers);
        return "customer-list";
    }

    private  List<CustomerResponseDto> filterCustomerDtoByDate(LocalDate fromLocalDate, LocalDate toLocalDate, List<CustomerResponseDto> allCustomers) {
        if (fromLocalDate != null && toLocalDate != null) {
            allCustomers = allCustomers.stream()
                    .filter(c ->
                            c.createdAt().isAfter(LocalDateTime.of(fromLocalDate, LocalTime.of(0, 0)))
                                    &&
                                    c.createdAt().isBefore(LocalDateTime.of(toLocalDate, LocalTime.of(23, 59)))
                    ).toList();
        }
        return allCustomers;
    }



    @GetMapping("/all-specialists")
    public String getAllSpecialists(Model model,  @RequestParam(name = "fromLocalDate", required = false) LocalDate fromLocalDate,
                                    @RequestParam(name = "toLocalDate", required = false) LocalDate toLocalDate) {
        String role = "ADMIN";
        if (!"ADMIN".equals(role)) {
            return "access-denied";
        }
        List<SpecialistResponseDto> allSpecialists = userService.getAllSpecialists();

        allSpecialists=filterSpecialistDtoByDate(fromLocalDate,toLocalDate,allSpecialists);
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
        Long countAllRequest = customerRequestService.countAllRequests();
        Long countAllCompletedOrders = orderService.countAllOrders();
        model.addAttribute("countAllRequest", countAllRequest);
        model.addAttribute("countAllCompletedOrders", countAllCompletedOrders);
        return " get-Profile-Admin";
    }



    @GetMapping("/searchOrders")
    public String searchOrders(
            @ModelAttribute("orderDtoSearch") OrderDtoSearch orderDtoSearch,
            Model model) {
        List<OrderDto> result = orderService.searchOrders(orderDtoSearch);

        model.addAttribute("orders", result);
        return "searchOrders";
    }

    private  List<SpecialistResponseDto> filterSpecialistDtoByDate(LocalDate fromLocalDate, LocalDate toLocalDate, List<SpecialistResponseDto> specialistResponseDtos) {
        if (fromLocalDate != null && toLocalDate != null) {
            specialistResponseDtos = specialistResponseDtos.stream()
                    .filter(c ->
                            c.createdAt().isAfter(LocalDateTime.of(fromLocalDate, LocalTime.of(0, 0)))
                                    &&
                                    c.createdAt().isBefore(LocalDateTime.of(toLocalDate, LocalTime.of(23, 59)))
                    ).toList();
        }
        return specialistResponseDtos;
    }
}
