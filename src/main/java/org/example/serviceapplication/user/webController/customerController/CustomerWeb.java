package org.example.serviceapplication.user.webController.customerController;


import org.example.serviceapplication.card.model.CardDto;
import org.example.serviceapplication.card.service.CardService;
import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.offer.service.OfferServiceInterface;
import org.example.serviceapplication.order.model.OrderDto;
import org.example.serviceapplication.order.service.OrderService;
import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.request.exception.RequestNotPresent;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.request.sercvice.CustomerRequestService;
import org.example.serviceapplication.review.model.ReviewDto;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.exception.UserHasWrongRole;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.UserService;
import org.example.serviceapplication.user.service.customerService.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class CustomerWeb {
    private final Logger logger = LoggerFactory.getLogger(CustomerWeb.class);
    private final SubServiceCategoryInterface subService;
    private final CustomerService customerService;
    private final CustomerRequestService customerRequestService;
    private final OfferServiceInterface offerService;
    private final CardService cardService;
    private final UserService userService;
    private final OrderService orderService;

    public CustomerWeb(SubServiceCategoryInterface subService, CustomerService customerService, CustomerRequestService customerRequestService, OfferServiceInterface offerService, CardService cardService, UserService userService, OrderService orderService) {
        this.subService = subService;
        this.customerService = customerService;
        this.customerRequestService = customerRequestService;
        this.offerService = offerService;
        this.cardService = cardService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/services")
    public String showServicesPage(Model model) {
        model.addAttribute("showList", false);
        return "services";
    }

    @GetMapping("/servicesList")
    public String showAllServices(Model model) {
        logger.info("Show all services");
        List<SubServiceCategories> services = subService.getAllSubServiceCatgories();
        Map<String, List<SubServiceCategories>> collect = services.stream().
                collect(Collectors.groupingBy(SubServiceCategories::categoryName));

        model.addAttribute("subCategories", collect);
        model.addAttribute("services", services);
        model.addAttribute("showList", true);
        logger.info("Returning  all services");
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
            logger.error("User has wrong role");
            throw new UserHasWrongRole("User has wrong role");

        }

        customerService.createRequest(customer, customerRequest);
        return "redirect:/customerRequests";
    }

    @GetMapping("/all-my-suggestions")
    public String showSuggestions() {
        return "all-my-suggestions";
    }


    @GetMapping("/offersDisplay")
    public String getAllOffersSorted(
            @RequestParam(name = "customer_request_id") Long customerRequestId,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            Model model) {

        CustomerRequest request = customerRequestService.findRequestById(customerRequestId);
        if (request == null) {
            throw new RequestNotPresent("Customer request not found for ID: " + customerRequestId);
        }

        model.addAttribute("customerRequestId", customerRequestId);
        List<OfferDto> allOffers;
        if ("price".equalsIgnoreCase(sortBy)) {
            allOffers = offerService.getAllOffersSortedByPrice(request);
        } else {
            allOffers = offerService.getAllOffersNotSorted(request);
        }

        model.addAttribute("offers", allOffers);
        return "offersDisplay";
    }


    @GetMapping("/place-order")
    public String showOrderForm() {
        return "place-order";
    }

    @PostMapping("/submit-order")
    public String createOrder(@ModelAttribute OrderDto orderDto, Model model) {
        Long idUser = orderDto.customerId();
        User customer = customerService.getUserById(idUser);
        if (idUser == null) {
            model.addAttribute("errorMessage", "شناسه مشتری نامعتبر است.");
            return "place-order";
        }

        if (customer.getRole() != Role.Customer) {
            model.addAttribute("errorMessage", "کاربر دارای نقش نادرست است.");
            return "place-order";
        }
        customerService.createOrder(customer, orderDto);
        model.addAttribute("successMessage", "سفارش با موفقیت ثبت شد!");
        return "services";
    }

    @GetMapping("/customer-profile")
    public String showCustomerProfile(Model model) {
        model.addAttribute("reviewDto", new ReviewDto(null,
                null, null, 0, null));
        return "customer-profile";
    }
    @PostMapping("/customer-profile")
    public String submitReview(@ModelAttribute ReviewDto reviewDto, RedirectAttributes redirectAttributes) {
        User customer = customerService.getUserById(reviewDto.customerId());

        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }

        customerService.addReview(customer, reviewDto);
        redirectAttributes.addFlashAttribute("successMessage",
                "نظر شما با موفقیت ثبت شد!");
        return "redirect:/customer-profile";
    }

    @GetMapping("/add-customer-card-form")
    public String showAddCardForm(Model model) {
        model.addAttribute("cardForm", new CardDto(null,
                null,
                null,
                null,
                null
                ));
        return "add-customer-card-form";
    }

    @PostMapping("/add-customer-card-form")
    public String addCustomerCard(@ModelAttribute("cardForm") CardDto cardDto, Model model) {
        User customer = userService.getUserById(cardDto.customerId());
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }
        cardService.createCard(cardDto);
        model.addAttribute("cardForm", cardDto);
        model.addAttribute("message", "کارت با موفقیت اضافه شد!");
        return "services";
    }


    @GetMapping("/enter-customer-id")
    public String enterCustomerIdForm() {
        return "enter-customer-id-form";
    }

    @GetMapping("/order_list")
    public String getOrdersByCustomerId(@RequestParam Long customerId, Model model) {
        User customer = customerService.getUserById(customerId);
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }
        List<OrderDto> ordersByCustomerId = orderService.getOrdersByCustomerId(customerId);
        model.addAttribute("orders", ordersByCustomerId);
        return "order_list";
    }


}