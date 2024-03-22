package com.app.zhardem.controllers;

import com.app.zhardem.services.impl.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaypalService paypalService;

    @PostMapping("/payment/create")
    public String createPayment() {
        try {
            String cancelURL = "http://localhost:8080/payment/cancel";
            String successURL = "http://localhost:8080/payment/success";

            Payment payment = paypalService.createPayment(
                    10.0,
                    "USD",
                    "paypal",
                    "sale",
                    "Payment description",
                    cancelURL,
                    successURL
            );

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return "redirect:" + links.getHref();
                }
                log.info(links.getRel());
            }
        } catch (PayPalRESTException restException) {
            log.error(restException.toString());
        }
        return "redirect:/payment/cancel";
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("payerId") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId,payerId);
            if(payment.getState().equals("approved")) {
                return "Success";
            }
        } catch (PayPalRESTException payPalRESTException){
            log.error(payPalRESTException.toString());
        }
        return "Success";
    }


}
