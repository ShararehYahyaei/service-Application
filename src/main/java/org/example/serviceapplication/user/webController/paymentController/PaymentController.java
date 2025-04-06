package org.example.serviceapplication.user.webController.paymentController;


import org.example.serviceapplication.card.model.CardDto;
import org.example.serviceapplication.card.model.CardResponse;
import org.example.serviceapplication.card.service.CardService;
import org.example.serviceapplication.user.service.customerService.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PaymentController {
    private final CardService cardService;
    private final CustomerService customerService;

    public PaymentController(CardService cardService, CustomerService customerService) {
        this.cardService = cardService;
        this.customerService = customerService;

    }

    @PostMapping("/payment")
    public String showPaymentOptions(@RequestParam("customerId") Long customerId,
                                     Model model) {
        List<CardResponse> cards = cardService.getCardsByCustomerId(customerId);
        model.addAttribute("cards", cards);
        model.addAttribute("customerId", customerId);
        return "payment-method";
    }

    @PostMapping("/payment-options")
    public String processPayment(@RequestParam String paymentMethod,
                                 @RequestParam Long customerId,
                                 Model model) {
        if (paymentMethod.equals("card")) {
            List<CardResponse> cards = cardService.getCardsByCustomerId(customerId);

            for (CardResponse card : cards) {
                System.out.println(card);
            }
            model.addAttribute("cards", cards);
            model.addAttribute("customerId", customerId);
            return "select-card";
        } else if (paymentMethod.equals("credit")) {
            return "redirect:/process-credit-payment";
        } else {
            return "redirect:/payment";
        }
    }

    @PostMapping("/card-details")
    public String showPaymentPage(@RequestParam Long cardId,
                                  @RequestParam Long customerId, Model model) {
        CardResponse card = cardService.getCardById(cardId);
        model.addAttribute("card", card);
        model.addAttribute("customerId", customerId);
        return "payment-details";
    }

    @PostMapping("/process-payment")
    public String processPayment(@RequestParam Long cardId, @RequestParam Long customerId,
                                 @RequestParam double amount, Model model) {

        CardResponse card = cardService.getCardById(cardId);
        if (card.balance() >= amount) {
            cardService.deductAmount(cardId, amount);
            model.addAttribute("message", "پرداخت با موفقیت انجام شد.");
        } else {
            model.addAttribute("message", "موجودی کافی نیست.");
        }
        return "payment-success";
    }


//    @PostMapping("/process-payment")
//    public String processCardPayment(@RequestParam Long selectedCard, Model model) {
//
//        boolean paymentSuccess = cardService.processPayment(selectedCard);
//
//        if (paymentSuccess) {
//            model.addAttribute("message", "پرداخت با موفقیت انجام شد!");
//        } else {
//            model.addAttribute("message", "پرداخت با خطا مواجه شد.");
//        }
//        return "payment-result";
//    }
//
//    @GetMapping("/process-credit-payment")
//    public String processCreditPayment(Model model) {
//        boolean paymentSuccess = customerService.deductCredit();
//        if (paymentSuccess) {
//            model.addAttribute("message", "پرداخت از اعتبار با موفقیت انجام شد!");
//        } else {
//            model.addAttribute("message", "موجودی اعتبار کافی نیست.");
//        }
//        return "payment-result";
//    }
}