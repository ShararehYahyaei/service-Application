package org.example.serviceapplication.user.webController.customerController;


import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.exception.UserHasWrongRole;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.customerService.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CustomerWeb {

    private final SubServiceCategoryInterface subService;
    private final CustomerService customerService;

    public CustomerWeb(SubServiceCategoryInterface subService, CustomerService customerService) {
        this.subService = subService;
        this.customerService = customerService;
    }

    @GetMapping("/services")
    public String showServicesPage(Model model) {
        model.addAttribute("showList", false);
        return "services";
    }

    @GetMapping("/servicesList")
    public String showAllServices(Model model) {
        List<SubServiceCategories> services = subService.getAllSubServiceCatgories();
        Map<String, List<SubServiceCategories>> collect = services.stream().
                collect(Collectors.groupingBy(SubServiceCategories::categoryName));

        model.addAttribute("subCategories", collect);
        model.addAttribute("services", services);
        model.addAttribute("showList", true);
        return "services";
    }


    @GetMapping("/customerRequests")
    public String showCustomerRequestPage(Model model) {
        model.addAttribute("customerRequestDto", new CustomerRequestDto(null,
                null, 0.0, "", null, ""));
        return "customerRequests";
    }

    @PostMapping("/customerRequests")
    public String createRequest(@ModelAttribute CustomerRequestDto customerRequest, Model model) {
        Long idUser = customerRequest.customerId();
        User customer = customerService.getUserById(idUser);
        model.addAttribute("customer", customer);
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }

        customerService.createRequest(customer, customerRequest);
        return "redirect:/customerRequests";
    }

}
