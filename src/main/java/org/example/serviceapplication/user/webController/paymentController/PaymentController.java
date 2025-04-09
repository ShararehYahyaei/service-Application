package org.example.serviceapplication.user.webController.paymentController;


import jakarta.servlet.http.HttpSession;
import org.example.serviceapplication.card.exception.*;
import org.example.serviceapplication.card.model.Card;
import org.example.serviceapplication.card.model.CardDto;
import org.example.serviceapplication.card.model.CardResponse;
import org.example.serviceapplication.card.service.CardService;
import org.example.serviceapplication.credit.exception.CreditIsNotSufficent;
import org.example.serviceapplication.credit.exception.CreditNotFoundException;
import org.example.serviceapplication.credit.model.Credit;
import org.example.serviceapplication.credit.model.CreditDto;
import org.example.serviceapplication.credit.model.CreditStatus;
import org.example.serviceapplication.credit.service.CreditService;
import org.example.serviceapplication.offer.model.Offer;
import org.example.serviceapplication.offer.service.OfferServiceInterface;
import org.example.serviceapplication.order.model.Order;
import org.example.serviceapplication.order.model.OrderStatus;
import org.example.serviceapplication.order.service.OrderService;
import org.example.serviceapplication.payment.PaymentService;
import org.example.serviceapplication.payment.PaymentServiceInterface;
import org.example.serviceapplication.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.*;
import java.util.List;
import java.util.Optional;

@Controller
public class PaymentController {
    private final CardService cardService;
    private final OfferServiceInterface offerService;
    private final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final OrderService orderService;
    private final PaymentServiceInterface paymentService;

    public PaymentController(CardService cardService, OfferServiceInterface offerService,
                             OrderService orderService, PaymentService paymentService, PaymentServiceInterface paymentService1) {
        this.cardService = cardService;
        this.offerService = offerService;
        this.orderService = orderService;
        this.paymentService = paymentService1;
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
            model.addAttribute("cards", cards);

            return "select-card";
        } else {
            paymentService.payByCustomerCredit(orderId, customerId);
        }
        model.addAttribute("customerId", customerId);
        model.addAttribute("orderId", orderId);
        return "services";
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
            User specialist = offer.getUser();
            cardService.widthraw(cardId, offer.getOfferPrice(), specialist,orderId);
            model.addAttribute("message", "پرداخت با موفقیت انجام شد.");
        } else {
            model.addAttribute("message", "موجودی کافی نیست.");
            throw new CardIsNotSufficent("موجودی کافی نیست.");
        }
        return "customer-profile";
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


}