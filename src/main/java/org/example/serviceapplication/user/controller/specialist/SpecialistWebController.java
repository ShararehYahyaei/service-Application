package org.example.serviceapplication.user.controller.specialist;

import org.example.serviceapplication.credit.service.CreditService;
import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.offer.service.OfferServiceInterface;
import org.example.serviceapplication.order.model.Order;
import org.example.serviceapplication.order.model.OrderDto;
import org.example.serviceapplication.order.service.OrderService;
import org.example.serviceapplication.request.dto.CustomerRequestResponseDto;
import org.example.serviceapplication.request.exception.RequestStatusIsNotCorrect;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.request.model.RequestStatus;
import org.example.serviceapplication.request.sercvice.CustomerRequestService;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.model.SubServiceCategory;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.exception.UserHasWrongRole;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.UserService;
import org.example.serviceapplication.user.service.specialistService.SpecialistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class SpecialistWebController {
    private final SubServiceCategoryInterface subservice;
    private final SpecialistService specialistService;
    private final UserService userService;
    private final OrderService orderService;
    private final OfferServiceInterface offerService;
    private final CreditService creditService;
    private final CustomerRequestService customerRequestService;


    public SpecialistWebController(SubServiceCategoryInterface subservice,
                                   SpecialistService specialistService, UserService userService, OrderService orderService, OfferServiceInterface offerService, CreditService creditService, CustomerRequestService customerRequestService) {
        this.subservice = subservice;
        this.specialistService = specialistService;
        this.userService = userService;
        this.orderService = orderService;
        this.offerService = offerService;
        this.creditService = creditService;
        this.customerRequestService = customerRequestService;
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
    public String showAllRequestsPage(@RequestParam(value = "userIdCredit", required = false) Long customerId, Model model) {
        User specialist = specialistService.getById(customerId);
        if (specialist.getRole() != Role.Specialist) {
            throw new UserHasWrongRole("User has wrong role");
        }

        List<CustomerRequestResponseDto> requests = specialistService.getAllRequests(specialist);
        List<Map<String, Object>> formattedRequests = requests.stream()
                .filter(request ->
                        request.requestStatus() != RequestStatus.AwaitingSpecialistArrival &&
                                request.requestStatus() != RequestStatus.InProgress
                )
                .map(request -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("requestNumber", request.requestNumber());
                    map.put("price", request.price());
                    map.put("description", request.description());
                    map.put("formattedDeadLineTime", request.deadLineTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    map.put("address", request.address());
                    map.put("requestStatus", request.requestStatus());
                    map.put("userIdCredit", customerId);
                    return map;
                })
                .toList();

        model.addAttribute("requests", formattedRequests);
        return "all-requests";
    }


    @GetMapping("/changeRequestStatus")
    public String showAllRequestsPageForChangeStatus(@RequestParam(value = "userIdCredit", required = false) Long customerId, Model model) {
        User specialist = specialistService.getById(customerId);
        if (specialist.getRole() != Role.Specialist) {
            throw new UserHasWrongRole("User has wrong role");
        }
        List<CustomerRequestResponseDto> requests = specialistService.getAllRequests(specialist);
        List<Map<String, Object>> formattedRequests = requests.stream()
                .filter(request -> request.requestStatus().equals(RequestStatus.AwaitingSpecialistArrival))
                .map(request -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("requestNumber", request.requestNumber());
                    map.put("price", request.price());
                    map.put("description", request.description());
                    map.put("formattedDeadLineTime", request.deadLineTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    map.put("address", request.address());
                    map.put("requestStatus", request.requestStatus());
                    map.put("userIdCredit", customerId);
                    return map;
                })
                .toList();

        model.addAttribute("requests", formattedRequests);
        return "changeRequestStatus";
    }


    @PostMapping("/change-status")
    public String changeRequestStatus(@RequestParam("requestNumber") String requestNumber) {
        Long idRequest = Long.parseLong(requestNumber);
        CustomerRequest requestById = customerRequestService.findRequestById(idRequest);
        if(requestById.getRequestStatus()== RequestStatus.AwaitingSpecialistArrival){
            customerRequestService.changeStatus(requestById);
            return "/services";
        }
        throw new RequestStatusIsNotCorrect("Request has no correct status");

    }


    @GetMapping("/offer")
    public String showOfferForm(@RequestParam(value = "userIdCredit", required = false) Long customerId, Model model) {
        OfferDto offerDto = new OfferDto(
                null,
                customerId,
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
    public String createOffer(@ModelAttribute OfferDto offerDto, Model model
            , RedirectAttributes redirectAttributes) {
        Long specialistId = offerDto.specialistId();
        User specialist = specialistService.getById(specialistId);
        model.addAttribute("specialist", offerDto);
        if (specialist.getRole() != Role.Specialist) {
            throw new UserHasWrongRole("Specialist has wrong role");
        }

        specialistService.createOffer(specialist, offerDto);
        redirectAttributes.addFlashAttribute("message", "پیشنهاد با موفقیت ثبت شد");
        return "redirect:/offer/success";
    }

    @GetMapping("/offer/success")
    public String offerSuccessPage() {
        return "offer-success";
    }

    @GetMapping("/assign-sub-service-to-specialist")
    public String assignSubServicePage(
            @RequestParam("userId") Long userId,
            Model model) {

        User specialist = userService.getUserById(userId);

        if (specialist == null) {
            model.addAttribute("error", "متخصص مورد نظر یافت نشد!");
            return "assign-sub-service";
        }

        model.addAttribute("specialist", specialist);
        return "assign-sub-service";
    }

    @PostMapping("/assign-sub-service-to-specialist")
    public String assignSubServiceToSpecialist(@RequestParam("userId") Long userId,
                                               @RequestParam("subServiceId") Long subServiceId) {


        User specialist = userService.getUserById(userId);
        SubServiceCategory subServiceCategoryById = subservice.getSubServiceCategoryById(subServiceId);
        specialist.getSubServiceCategories().add(subServiceCategoryById);

        userService.addSubCategory(userId, subServiceId);
        subServiceCategoryById.getUsers().add(specialist);
        return "redirect:/specialist-profile";
    }

    @GetMapping("/order_list_specialist")
    public String getOrdersBySpecialistId(@RequestParam(value = "userIdCredit", required = false) Long customerId, Model model) {
        User specialist = userService.getUserById(customerId);
        if (specialist.getRole() != Role.Specialist) {
            throw new UserHasWrongRole("User has wrong role");
        }

        List<OfferDto> allOffersBySpecialistId = offerService.getAllOffersBySpecialistId(specialist.getId());
        List<Long> collect = allOffersBySpecialistId.stream().map(c -> c.offerId()).collect(Collectors.toList());
        List<OrderDto> allOrdersForSpecialist = orderService.getAllOrdersForSpecialist(collect);
        model.addAttribute("specialist", specialist);
        model.addAttribute("allOrdersForSpecialist", allOrdersForSpecialist);
        return "order_list_specialist";
    }

    @GetMapping("/update-order-status")
    public String updateOrderStatus(@RequestParam(value = "orderId", required = false) Long orderId, Model model) {

        Order order = orderService.getOrderById(orderId);
        Offer offer = order.getOffer();
        orderService.changeOrderStatus(offer.getId());
        model.addAttribute("order", order);
        return "services";
    }


    @GetMapping("/addSubcategoryToSpecialist")
    public String showAddSubcategoryToSpecialistPage(Model model) {

        return "addSubcategoryToSpecialist";
    }

    @PostMapping("/addSubcategoryToSpecialist")
    public String addSubcategoryToSpecialistPage(@RequestParam(value = "userId", required = false) Long userId,
                                                 @RequestParam(value = "subCategoryId", required = false) Long subCategoryId, Model model) {
        userService.addSubCategory(userId, subCategoryId);
        return "/ get-Profile-Admin";
    }


}
