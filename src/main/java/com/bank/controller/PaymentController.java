package com.bank.controller;

import com.bank.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payment")
public class PaymentController {
    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/internet/inquiry")
    public String internetPaymentInquiry(@RequestBody String message) {
        return paymentService.processInternetInquiry(message);
//        return message;
    }

    @PostMapping(value = "/internet")
    public String internetPayment(@RequestBody String message) {
        return paymentService.processInternet(message);
//        return message;
    }
}
