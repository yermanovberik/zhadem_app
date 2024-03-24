package com.app.zhardem.controllers;

import com.app.zhardem.dto.category.CategoryRequestDto;
import com.app.zhardem.dto.category.CategoryResponseDto;
import com.app.zhardem.dto.doctor.DoctorRequestDto;
import com.app.zhardem.dto.doctor.DoctorResponseDto;
import com.app.zhardem.dto.payment.PaymentRequestDto;
import com.app.zhardem.dto.payment.PaymentResponseDto;
import com.app.zhardem.services.PaymentService;
import com.app.zhardem.services.impl.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PaypalService paypalService;
    private final PaymentService paymentService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponseDto getById(@PathVariable long id){
        return paymentService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDto createPayment(@RequestBody @Validated PaymentRequestDto requestDto){
        return paymentService.create(requestDto);
    }


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
                    return links.getHref();
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

    @PutMapping("/{id}")
    public PaymentResponseDto updatePayment(@PathVariable long id, @RequestBody @Valid PaymentRequestDto requestDto) {
        return paymentService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePayment(@PathVariable long id) {
        paymentService.delete(id);
    }

}
