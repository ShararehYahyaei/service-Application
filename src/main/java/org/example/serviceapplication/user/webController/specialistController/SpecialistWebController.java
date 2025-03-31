package org.example.serviceapplication.user.webController.specialistController;

import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class SpecialistWebController {
    private final SubServiceCategoryInterface subservice;

    public SpecialistWebController(SubServiceCategoryInterface subservice) {
        this.subservice = subservice;
    }


    @GetMapping("/sub-services")
    public String showSubServices(Model model) {
        List<SubServiceCategories> allSubServices = subservice.getAllSubServiceCatgories();
        model.addAttribute("allSubServices", allSubServices);
        return "sub-services";
    }

    @PostMapping("/add-subservice")
    public String addSubServiceRedirect() {
        return "redirect:/sub-services";
    }




}
