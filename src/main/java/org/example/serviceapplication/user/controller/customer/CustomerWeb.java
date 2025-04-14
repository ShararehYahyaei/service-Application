package org.example.serviceapplication.user.controller.customer;


import org.example.serviceapplication.card.model.CardDto;
import org.example.serviceapplication.card.service.CardService;
import org.example.serviceapplication.credit.model.Credit;
import org.example.serviceapplication.credit.model.CreditDto;
import org.example.serviceapplication.credit.service.CreditService;
import org.example.serviceapplication.location.service.LocationService;
import org.example.serviceapplication.offer.dto.OfferDto;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.offer.model.OfferStatus;
import org.example.serviceapplication.offer.service.OfferServiceInterface;
import org.example.serviceapplication.order.model.OrderDto;
import org.example.serviceapplication.order.service.OrderService;
import org.example.serviceapplication.request.dto.CustomerRequestDto;
import org.example.serviceapplication.request.exception.RequestNotPresent;
import org.example.serviceapplication.request.model.CustomerRequest;
import org.example.serviceapplication.request.sercvice.CustomerRequestService;
import org.example.serviceapplication.review.model.ReviewDto;
import org.example.serviceapplication.review.service.ReviewService;
import org.example.serviceapplication.subCategory.dto.SubServiceCategories;
import org.example.serviceapplication.subCategory.service.SubServiceCategoryInterface;
import org.example.serviceapplication.user.enumPackage.Role;
import org.example.serviceapplication.user.exception.UserHasWrongRole;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.UserService;
import org.example.serviceapplication.user.service.customerService.CustomerService;
import org.example.serviceapplication.workTimer.exception.NotWorkTimerForThisOffer;
import org.example.serviceapplication.workTimer.model.WorkTimer;
import org.example.serviceapplication.workTimer.service.WorkTimerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final CreditService creditService;
    private final ReviewService reviewService;
    private final WorkTimerService workTimerService;


    public CustomerWeb(SubServiceCategoryInterface subService, CustomerService customerService,
                       CustomerRequestService customerRequestService, OfferServiceInterface offerService,
                       CardService cardService, UserService userService, OrderService orderService,
                       CreditService creditService, ReviewService reviewService, WorkTimerService workTimerService) {
        this.subService = subService;
        this.customerService = customerService;
        this.customerRequestService = customerRequestService;
        this.offerService = offerService;
        this.cardService = cardService;
        this.userService = userService;
        this.orderService = orderService;
        this.creditService = creditService;
        this.reviewService = reviewService;

        this.workTimerService = workTimerService;
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
    public String showCustomerRequestPage(@RequestParam(value = "userIdCredit") Long userIdCredit,  Model model) {
        model.addAttribute("customerRequestDto", new CustomerRequestDto(userIdCredit,
                null, 0.0, "", null, "",
                35.6892, 51.3890));
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

        model.addAttribute("reviewDto", new ReviewDto(idUser,
                null, null, 0, null));
        return "/customer-profile";
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
    public String showCustomerProfile(@RequestParam(value = "customerId", required = false)Long customerId,
                                      Model model) {
        User user = userService.getUserById(customerId);
        model.addAttribute("reviewDto", new ReviewDto(customerId,
                null, null, 0, null));
        model.addAttribute("Role", user.getRole());
        if (user.getRole() == Role.Customer) {
           return "customer-profile";
        } else if (user.getRole() == Role.Specialist) {
            return "get-Specialist-profile";
        }
        throw new UserHasWrongRole("User has wrong role");
    }

    @PostMapping("/customer-profile")
    public String submitReview(@ModelAttribute ReviewDto reviewDto,Model model) {
        User customer = customerService.getUserById(reviewDto.customerId());

        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }

        customerService.addReview(customer, reviewDto);

        model.addAttribute("reviewDto", new ReviewDto(reviewDto.customerId(),
                null, null, 0, null));
        return "/customer-profile";
    }

    @GetMapping("/add-customer-card-form")
    public String showAddCardForm(@RequestParam(value = "userIdCredit") Long userIdCredit,Model model) {
        model.addAttribute("cardForm", new CardDto(null,
                null,
                null,
                null,
                userIdCredit
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

    @GetMapping("/addCredit")
    public String showAddCreditForm(@RequestParam(value = "userIdCredit") Long userIdCredit,
                                    @RequestParam(value = "Role") Role role,
                                    Model model) {
        model.addAttribute("creditForm", new CreditDto(userIdCredit, 0.0, null));
        model.addAttribute("Role", role);
        return "add-credit";
    }

    @PostMapping("/addCredit")
    public String addCredit(@ModelAttribute CreditDto creditDto, Model model) {
        logger.info("Received CreditDto: {}", creditDto);
        User user = userService.getUserById(creditDto.userId());
        if (user.getRole().equals(Role.Admin)) {
            logger.error("کاربر دارای نقش اشتباه است");
            model.addAttribute("message", "این کاربر اجازه افزودن اعتبار ندارد.");
            return "add-credit";
        }
        Optional<Credit> creditForUser = creditService.getCreditByUserId(user.getId());
        if (creditForUser.isPresent()) {
            creditForUser.get().setBalance(creditForUser.get().getBalance() + creditDto.balance());
            creditService.updareCredit(creditForUser.get());
            return "services";
        }
        creditService.createCredit(creditDto);
        return "services";

    }



    @GetMapping("/enter-customer-id")
    public String enterCustomerIdForm(   @RequestParam(value = "userIdCredit") Long userIdCredit) {
        return "enter-customer-id-form";
    }

    @GetMapping("/order_list")
    public String getOrdersByCustomerId(@RequestParam(value = "userIdCredit") Long userIdCredit, Model model) {
        User customer = customerService.getUserById(userIdCredit);
        if (customer.getRole() != Role.Customer) {
            throw new UserHasWrongRole("User has wrong role");
        }
        List<OrderDto> ordersByCustomerId = orderService.getOrdersByCustomerId(userIdCredit);
        model.addAttribute("orders", ordersByCustomerId);
        return "order_list";
    }

    @GetMapping("/rate-form")
    public String showRateForm(Model model) {
        model.addAttribute("userId", 0);
        return "rate-form";
    }


    @PostMapping("/rate-form")
    public String getRate(@RequestParam("userId") Long userId, Model model) {
        try {
            User user = userService.getUserById(userId);
            if (user.getRole() != Role.Specialist) {
                model.addAttribute("error", "کاربر نقش متخصص ندارد.");
                return "rate-form";
            }

            Double rate = reviewService.getRateForUser(userId);
            model.addAttribute("rate", rate);
        } catch (Exception e) {
            model.addAttribute("error", "خطا در پردازش: " + e.getMessage());
        }

        return "rate-form";
    }



    @GetMapping("/show-timer-form")
    public String showTimerForm() {
        return "show-timer";
    }

    @GetMapping("/show-timer")
    public String showRemainingTime(@RequestParam("requestId") Long requestId, Model model) {
        CustomerRequest request = customerRequestService.findRequestById(requestId);

        if (request == null) {
            model.addAttribute("remainingTime", "درخواست یافت نشد");
            return "services";
        }

        Offer offer = offerService.getOfferBYCustomerRequestAndStatus(request, OfferStatus.ACCEPTED);
        System.out.println(offer.toString()+"snvdhvbdhbv");
        if (offer == null) {
            model.addAttribute("remainingTime", "پیشنهاد تایید شده‌ای یافت نشد");
            return "time";
        }

        try {

            WorkTimer timer = workTimerService.getByOffer(offer.getId());
            String formattedTime = workTimerService.getRemainingTimeFormatted(timer);
            model.addAttribute("remainingTime", formattedTime);
            return "time";
        } catch (NotWorkTimerForThisOffer e) {
            model.addAttribute("remainingTime", "تایمر برای این پیشنهاد فعال نشده است");
        }

        return "time";
    }





}