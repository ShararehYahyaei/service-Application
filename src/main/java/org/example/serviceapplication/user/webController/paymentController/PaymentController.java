package org.example.serviceapplication.user.webController.paymentController;


import jakarta.servlet.http.HttpSession;
import org.example.serviceapplication.card.exception.CardInformationIsNotCorrect;
import org.example.serviceapplication.card.exception.CardIsExpired;
import org.example.serviceapplication.card.exception.CardIsNotFound;
import org.example.serviceapplication.card.model.Card;
import org.example.serviceapplication.card.model.CardDto;
import org.example.serviceapplication.card.model.CardResponse;
import org.example.serviceapplication.card.service.CardService;
import org.example.serviceapplication.credit.service.CreditService;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.offer.service.OfferServiceInterface;
import org.example.serviceapplication.order.model.Order;
import org.example.serviceapplication.order.model.OrderStatus;
import org.example.serviceapplication.order.service.OrderService;
import org.example.serviceapplication.user.model.User;
import org.example.serviceapplication.user.service.customerService.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class PaymentController {
    private final CardService cardService;
    private final OfferServiceInterface offerService;
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final OrderService orderService;
    private final CreditService creditService;

    public PaymentController(CardService cardService, OfferServiceInterface offerService,
                             OrderService orderService, CreditService creditService) {
        this.cardService = cardService;
        this.creditService = creditService;
        this.offerService = offerService;
        this.orderService = orderService;
    }

    @PostMapping("/payment")
    public String showPaymentOptions(@RequestParam Long customerId,
                                     @RequestParam OrderStatus status,
                                     @RequestParam Long orderId,
                                     Model model) {
        if (status == OrderStatus.COMPLETED) {
            List<CardResponse> cards = cardService.getCardsByCustomerId(customerId);
            if (cards.isEmpty()) {
                model.addAttribute("message", "شما هیچ کارتی ندارید. لطفا کارت خود را اضافه کنید.");
                model.addAttribute("cardForm", new CardDto(null,
                        null,
                        null,
                        null,
                        null
                ));
                return "add-customer-card-form";
            }
            model.addAttribute("cards", cards);
            model.addAttribute("customerId", customerId);
            model.addAttribute("orderStatus", status);
            model.addAttribute("orderId", orderId);
            System.out.println("orderId: " + orderId);
            return "payment-method";
        } else {
            model.addAttribute("message", "not completed yet ...");
        }
        return "payment-method";
    }

    @PostMapping("/payment-options")
    public String processPayment(@RequestParam String paymentMethod,
                                 @RequestParam Long customerId,
                                 @RequestParam Long orderId,
                                 Model model) {
        if (paymentMethod.equals("card")) {
            List<CardResponse> cards = cardService.getCardsByCustomerId(customerId);
            for (CardResponse card : cards) {
                System.out.println(card);
            }
            model.addAttribute("cards", cards);
            model.addAttribute("customerId", customerId);
            model.addAttribute("orderId", orderId);
            return "select-card";
        } else if (paymentMethod.equals("credit")) {
            return "redirect:/process-credit-payment";
        } else {
            return "redirect:/payment";
        }
    }

    @PostMapping("/card-details")
    public String showPaymentPage(@RequestParam Long cardId,
                                  @RequestParam Long customerId,
                                  @RequestParam Long orderId,
                                  Model model) {
        CardResponse card = cardService.getCardById(cardId);
        model.addAttribute("card", card);
        model.addAttribute("customerId", customerId);
        model.addAttribute("orderId", orderId);
        return "payment-details";
    }

    @PostMapping("/process-payment")
    public String processPayment(@RequestParam Long cardId, String cvv,
                                 @RequestParam LocalDate expiryDate,
                                 @RequestParam String captcha,
                                 HttpSession session,
                                 @RequestParam Long orderId,
                                 Model model) {
        Card card = cardService.getByIdCard(cardId);
        if (card == null || cvv == null) {
            logger.error("Card or cvv is null");
            throw new CardIsNotFound("Card not found");
        }
        if (!card.getCvv().equals(cvv)) {
            logger.error("Card or cvv does not match");
            throw new CardInformationIsNotCorrect("Card or cvv is incorrect");
        }
        if(!card.getExpirationDate().equals(expiryDate)) {
            throw new CardIsExpired("card date is not correct");
        }

        String sessionCaptcha = (String) session.getAttribute("captcha");
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            model.addAttribute("error", "کد امنیتی اشتباه است.");
            return "payment"; // نام فایل html
        }

        Order order = orderService.getOrderById(orderId);
        Offer offerById = offerService.getOfferById(order.getOffer().getId());
        if (card.getAmount() >= offerById.getOfferPrice()) {

            model.addAttribute("orderId", orderId);
            User user = offerById.getUser();
            cardService.deductAmount(cardId, offerById.getOfferPrice(),user);
            model.addAttribute("message", "پرداخت با موفقیت انجام شد.");
        } else {
            model.addAttribute("message", "موجودی کافی نیست.");
        }
        return "payment-success";
    }

//    @GetMapping("/process-credit-payment")
//    public String processCreditPayment(
//            @RequestParam Long orderId,
//            Model model) {
//   // boolean paymentSuccess = creditService.getCreditByUserId();
////        if (paymentSuccess) {
////            model.addAttribute("message", "پرداخت از اعتبار با موفقیت انجام شد!");
////        } else {
////            model.addAttribute("message", "موجودی اعتبار کافی نیست.");
////        }
////        return "payment-result";
//        model.addAttribute("orderId", orderId);
//        System.out.println("orderId: " + orderId);
//        return "payment-method";
//    }
}