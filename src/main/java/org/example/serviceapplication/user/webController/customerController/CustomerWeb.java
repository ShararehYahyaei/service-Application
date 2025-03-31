package org.example.serviceapplication.user.webController.customerController;


import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CustomerWeb {

    private final SubServiceCategoryInterface subService;

    public CustomerWeb(SubServiceCategoryInterface subService) {
        this.subService = subService;
    }

    @GetMapping("/services")
    public String showServicesPage(Model model) {
        model.addAttribute("showList", false);
        return "services";
    }

    @GetMapping("/servicesList")
    public String showAllServices(Model model) {
        List<SubServiceCategories> services = subService.getAllSubServiceCatgories();
        Map<String, List<SubServiceCategories>> collect =services.stream().
                collect(Collectors.groupingBy(SubServiceCategories::categoryName));

        model.addAttribute("subCategories", collect);
        model.addAttribute("services", services);
        model.addAttribute("showList", true);
        return "services";
    }
}
