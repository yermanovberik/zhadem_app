package com.app.zhardem.controllers;

import com.app.zhardem.dto.CreatePaymentRequest;
import com.app.zhardem.dto.StripeResponses;
import com.app.zhardem.services.impl.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.StripeResponse;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stripe")
@RequiredArgsConstructor
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/create-payment")
    public ResponseEntity<StripeResponses> createPayment(@RequestBody CreatePaymentRequest createPaymentRequest) {
        StripeResponses stripeResponse = stripeService.createPayment(createPaymentRequest);
        return ResponseEntity
                .status(stripeResponse.getHttpStatus())
                .body(stripeResponse);
    }

    @GetMapping("/capture-payment")
    public ResponseEntity<StripeResponses> capturePayment(@RequestParam String sessionId) {
        StripeResponses stripeResponse = stripeService.capturePayment(sessionId);
        return ResponseEntity
                .status(stripeResponse.getHttpStatus())
                .body(stripeResponse);
    }
}