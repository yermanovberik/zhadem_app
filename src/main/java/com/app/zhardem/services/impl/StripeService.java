package com.app.zhardem.services.impl;

import com.app.zhardem.dto.CapturePaymentResponse;
import com.app.zhardem.dto.CreatePaymentRequest;
import com.app.zhardem.dto.CreatePaymentResponse;
import com.app.zhardem.dto.StripeResponses;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class StripeService {

    public StripeResponses createPayment(CreatePaymentRequest createPaymentRequest) {
        Stripe.apiKey = "sk_test_51NvFT9DMd4BkEMbZ6ui5Cm5kCG7PVY07WleHTmmvOXmydkZsYLne6YrjiJqjIjVFZyYiYBzPvApOw5mrR4SkIs3i00Oo9ETQ5L";

        // Create a PaymentIntent with the order amount and currency
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(createPaymentRequest.getName())
                        .build();

        // Create new line item with the above product data and associated price
        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(createPaymentRequest.getCurrency())
                        .setUnitAmount(createPaymentRequest.getAmount())
                        .setProductData(productData)
                        .build();

        // Create new line item with the above price data
        SessionCreateParams.LineItem lineItem =
                SessionCreateParams
                        .LineItem.builder()
                        .setQuantity(createPaymentRequest.getQuantity())
                        .setPriceData(priceData)
                        .build();

        // Create new session with the line items
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(createPaymentRequest.getSuccessUrl())
                        .setCancelUrl(createPaymentRequest.getCancelUrl())
                        .addLineItem(lineItem)
                        .build();

        // Create new session
        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            e.printStackTrace();
            StripeResponses stripeResponses = new StripeResponses("Failure","Payment session creation failed",400,null);
            return stripeResponses;
        }

        CreatePaymentResponse responseData = CreatePaymentResponse
                .builder()
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();

        StripeResponses stripeResponses = new StripeResponses("Success","Payment session created successfully",200,responseData);

        return stripeResponses;
    }

    public StripeResponses capturePayment(String sessionId) {
        Stripe.apiKey = "sk_test_51NvFT9DMd4BkEMbZ6ui5Cm5kCG7PVY07WleHTmmvOXmydkZsYLne6YrjiJqjIjVFZyYiYBzPvApOw5mrR4SkIs3i00Oo9ETQ5L";

        try {
            Session session = Session.retrieve(sessionId);
            String status = session.getStatus();

            if (status.equalsIgnoreCase("200")) {
                // Handle logic for successful payment
                log.info("Payment successfully captured.");
            } // Handle more statuses

            CapturePaymentResponse responseData = CapturePaymentResponse
                    .builder()
                    .sessionId(sessionId)
                    .sessionStatus(status)
                    .paymentStatus(session.getPaymentStatus())
                    .build();

            StripeResponses stripeResponses = new StripeResponses("Success","Payment successfully captured",200,responseData);

            return stripeResponses;
        } catch (StripeException e) {
            e.printStackTrace();
            StripeResponses stripeResponses = new StripeResponses("FAilure","Payment capture failed due to a server error.",500,null);
            return stripeResponses;
        }
    }
}
