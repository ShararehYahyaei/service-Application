package org.example.serviceapplication.user.webController.AdminController;

import org.example.serviceapplication.user.dto.CustomerResponseDto;
import org.example.serviceapplication.user.dto.SpecialistResponseDto;
import org.example.serviceapplication.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminWebController {

    private final UserService userService;


    public AdminWebController(UserService userService) {
        this.userService = userService;
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

}
