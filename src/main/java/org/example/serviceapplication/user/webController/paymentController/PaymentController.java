package org.example.serviceapplication.user.webController.paymentController;


import jakarta.servlet.http.HttpSession;
import org.example.serviceapplication.card.exception.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.*;
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
            model.addAttribute("customerId", customerId);
            return "redirect:/process-credit-payment";
        } else {
            return "redirect:/payment";
        }
    }

    @PostMapping("/card-details")
    public String showPaymentPage(@RequestParam Long cardId,
                                  @RequestParam Long customerId,
                                  @RequestParam Long orderId,
                                  HttpSession session,
                                  Model model) {
        CardResponse card = cardService.getCardById(cardId);
        model.addAttribute("card", card);
        model.addAttribute("customerId", customerId);
        model.addAttribute("orderId", orderId);
        session.setAttribute("entryTime", System.currentTimeMillis());
        return "payment-details";
    }

    //todo create dto for these  request param
    @PostMapping("/process-payment")
    public String processPayment(@RequestParam Long cardId, String cvv,
                                 @RequestParam LocalDate expiryDate,
                                 @RequestParam String captcha,
                                 HttpSession session,
                                 @RequestParam Long orderId,
                                 Model model) {
        Card card = cardService.getByIdCard(cardId);
        validationCard(cvv, expiryDate, card);
        String sessionCaptcha = (String) session.getAttribute("captcha");
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            throw new CardValidationException("کد امنیتی اشتباه است.");
        }

        checkPageTimeOut(session, model);
        Order order = orderService.getOrderById(orderId);
        Offer offer = offerService.getOfferById(order.getOffer().getId());
        model.addAttribute("orderId", orderId);
        if (card.getAmount() >= offer.getOfferPrice()) {
            User user = offer.getUser();
            cardService.widthraw(cardId, offer.getOfferPrice(), user);
            model.addAttribute("message", "پرداخت با موفقیت انجام شد.");
        } else {
            model.addAttribute("message", "موجودی کافی نیست.");
            throw new CardIsNotSufficent("موجودی کافی نیست.");
        }
        return "payment-success";
    }

    private void validationCard(String cvv, LocalDate expiryDate, Card card) {
        if (card == null || cvv == null) {
            logger.error("Card or cvv is null");
            throw new CardIsNotFound("Card not found");
        }
        if (!card.getCvv().equals(cvv)) {
            logger.error("Card or cvv does not match");
            throw new CardInformationIsNotCorrect("Card or cvv is incorrect");
        }
        if (!card.getExpirationDate().equals(expiryDate)) {
            throw new CardIsExpired("card date is not correct");
        }
    }

    private static void checkPageTimeOut(HttpSession session, Model model) {
        Long entryTime = (Long) session.getAttribute("entryTime");

        Duration between = Duration.between(LocalDateTime.ofInstant(Instant.ofEpochMilli(entryTime),
                ZoneId.systemDefault()), LocalDateTime.now());

        if (entryTime == null || between.toMinutes() > 1) {
            model.addAttribute("error", "مهلت پرداخت شما به پایان رسیده است. لطفاً دوباره تلاش کنید.");
            throw new IllegalStateException("time uit");

        }
    }

//    @GetMapping("/process-credit-payment")
//    public String processCreditPayment(
//            @RequestParam Long orderId,
//            @RequestParam Long customerId,
//            Model model) {
//
//        Order order = orderService.getOrderById(orderId);
//        double offerPrice = order.getOffer().getOfferPrice();
//        Optional<Credit> credit = creditService.getCreditByUserId(customerId);
//        if (credit.isEmpty()) {
//            throw new CreditNotFoundException("Credit not found");
//        }
//        if (credit.get().getBalance() < offerPrice) {
//            throw new CreditIsNotSufficent("credit is not sufficient to credit");
//        }
//        double amountNew = offerPrice * 0.70;
//        credit.get().setBalance(credit.get().getBalance() -offerPrice);
//        offerService.getOfferById()
//        creditService.updareCredit(credit.get());
//        model.addAttribute("message", "پرداخت از اعتبار با موفقیت انجام شد!");
//
//
//        return "payment-result";
//        model.addAttribute("orderId", orderId);
//        System.out.println("orderId: " + orderId);
//        return "payment-method";
//    }
}