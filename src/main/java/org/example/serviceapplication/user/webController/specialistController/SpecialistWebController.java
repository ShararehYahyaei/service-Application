package org.example.serviceapplication.user.webController.specialistController;

import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.request.dto.CustomerRequestResponseDto;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.exception.UserHasWrongRole;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.specialistService.SpecialistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SpecialistWebController {
    private final SubServiceCategoryInterface subservice;
    private final SpecialistService specialistService;


    public SpecialistWebController(SubServiceCategoryInterface subservice,
                                   SpecialistService specialistService) {
        this.subservice = subservice;
        this.specialistService = specialistService;
    }


    @GetMapping("/sub-services")
    public String showSubServices(Model model) {
        List<SubServiceCategories> allSubServices = subservice.getAllSubServiceCatgories();
        model.addAttribute("allSubServices", allSubServices);
        return "sub-services";
    }

    @PostMapping("/add-sub-service")
    public String addSubServiceRedirect() {
        return "redirect:/sub-services";
    }


    @GetMapping("/requests-form")
    public String showRequestForm() {
        return "requestsForm";
    }

    @GetMapping("/all-requests")
    public String showAllRequestsPage(@RequestParam("userId") Long userId, Model model) {
        User specialist = specialistService.getById(userId);

        if (specialist.getRole() != Role.Specialist) {
            throw new UserHasWrongRole("User has wrong role");
        }

        List<CustomerRequestResponseDto> requests = specialistService.getAllRequests(specialist);
        List<Map<String, Object>> formattedRequests = requests.stream().map(request -> {
            Map<String, Object> map = new HashMap<>();
            map.put("requestNumber", request.requestNumber());
            map.put("price", request.price());
            map.put("description", request.description());
            map.put("formattedDeadLineTime", request.deadLineTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            map.put("address", request.address());
            map.put("requestStatus", request.requestStatus());
            return map;
        }).toList();
        model.addAttribute("requests", formattedRequests);
        return "all-requests";
    }


    @GetMapping("/offer")
    public String showOfferForm(Model model) {
        OfferDto offerDto = new OfferDto(
                null,
                null,
                0.0,
                LocalDate.now(),
                0,
                null,
                null
        );


        model.addAttribute("offer", offerDto);
        return "offer";
    }

    @PostMapping("/offer")
    public String createOffer(@ModelAttribute OfferDto offerDto, Model model) {
        Long specialistId = offerDto.specialistId();
        User specialist = specialistService.getById(specialistId);
        model.addAttribute("specialist", offerDto);
        if (specialist.getRole() != Role.Specialist) {
            throw new UserHasWrongRole("Specialist has wrong role");
        }

        specialistService.createOffer(specialist, offerDto);
        return "redirect:/offer";
    }


}
