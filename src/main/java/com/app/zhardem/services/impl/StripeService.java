package com.app.zhardem.services.impl;

import com.app.zhardem.dto.CapturePaymentResponse;
import com.app.zhardem.dto.CreatePaymentRequest;
import com.app.zhardem.dto.CreatePaymentResponse;
import com.app.zhardem.dto.StripeResponses;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.Appointments;
import com.app.zhardem.models.Doctor;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.AppointmentsRepository;
import com.app.zhardem.repositories.DoctorRepository;
import com.app.zhardem.repositories.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class StripeService {
    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    public StripeResponses createPayment(CreatePaymentRequest createPaymentRequest) {
        Stripe.apiKey = "sk_test_51NvFT9DMd4BkEMbZ6ui5Cm5kCG7PVY07WleHTmmvOXmydkZsYLne6YrjiJqjIjVFZyYiYBzPvApOw5mrR4SkIs3i00Oo9ETQ5L";

        User user = userRepository.findById(createPaymentRequest.getUserID())
                .orElseThrow(() -> new EntityNotFoundException("User with this id " + createPaymentRequest.getUserID() + " not found!"));

        Doctor doctor = doctorRepository.findById(createPaymentRequest.getDoctorID())
                .orElseThrow(() -> new EntityNotFoundException("Doctor with this id " + createPaymentRequest.getDoctorID() + " not found!"));

        List<Appointments> appointmentsList = appointmentsRepository.findAppointmentsByUserAndDoctorAndDay(user.getId(), doctor.getId(), createPaymentRequest.getDayNumber());
        if (appointmentsList.isEmpty()) {
            throw new EntityNotFoundException("Appointment on day " + createPaymentRequest.getDayNumber() + " not found!");
        }
        Appointments appointments = appointmentsList.get(0);
        String name = doctor.getFullName() + " doctor is booking for the date " + appointments.getDate() + " on time " + appointments.getTime();
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(name)
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("USD")
                        .setUnitAmount(appointments.getAmountPaid())
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams
                        .LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();


        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:3000/home/")
                        .setCancelUrl("http://localhost:3000/booking/")
                        .addLineItem(lineItem)
                        .build();

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
                log.info("Payment successfully captured.");
            }
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
            StripeResponses stripeResponses = new StripeResponses("Failure","Payment capture failed due to a server error.",500,null);
            return stripeResponses;
        }
    }
}
