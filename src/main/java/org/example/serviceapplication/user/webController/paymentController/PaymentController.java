package org.example.serviceapplication.user.webController.paymentController;


import org.example.serviceapplication.card.service.CardService;
import org.example.serviceapplication.user.service.customerService.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {
    private final CardService cardService;
    private final CustomerService customerService;

    public PaymentController(CardService cardService, CustomerService customerService) {
        this.cardService = cardService;
        this.customerService = customerService;

    }

    @GetMapping("/payment")
    public String showPaymentOptions() {
        return "payment-method";
    }

//    @PostMapping("/payment")
//    public String processPayment(@RequestParam String paymentMethod, Model model) {
//        if (paymentMethod.equals("card")) {
//            // بارگذاری کارت‌های مشتری برای نمایش
//            List<Card> cards = cardService.getCustomerCards();
//            model.addAttribute("cards", cards);
//            return "select-card";
//        } else if (paymentMethod.equals("credit")) {
//            // پرداخت از اعتبار
//            return "redirect:/process-credit-payment";
//        } else {
//            return "redirect:/payment"; // در صورت انتخاب اشتباه
//        }
//    }
    @PostMapping("/process-payment")
    public String processCardPayment(@RequestParam Long selectedCard, Model model) {
        // انجام عملیات پرداخت از کارت
        boolean paymentSuccess = cardService.processPayment(selectedCard);

        if (paymentSuccess) {
            model.addAttribute("message", "پرداخت با موفقیت انجام شد!");
        } else {
            model.addAttribute("message", "پرداخت با خطا مواجه شد.");
        }
        return "payment-result";
    }
//
//    @GetMapping("/process-credit-payment")
//    public String processCreditPayment(Model model) {
//        boolean paymentSuccess = customerService.deductCredit();
//
//        if (paymentSuccess) {
//            model.addAttribute("message", "پرداخت از اعتبار با موفقیت انجام شد!");
//        } else {
//            model.addAttribute("message", "موجودی اعتبار کافی نیست.");
//        }
//        return "payment-result";
//    }
}