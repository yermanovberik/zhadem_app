package com.app.zhardem.services.impl;

import com.app.zhardem.dto.payment.PaymentRequestDto;
import com.app.zhardem.dto.payment.PaymentResponseDto;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.Payment;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.PaymentRepository;
import com.app.zhardem.repositories.UserRepository;
import com.app.zhardem.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    @Override
    public PaymentResponseDto getById(long id) {
        Payment payment = getEntityById(id);

        PaymentResponseDto responseDto = PaymentResponseDto.builder()
                .cvv(payment.getCvv())
                .expirationDate(payment.getExpirationDate())
                .cardNumber(payment.getCardNumber())
                .userID(payment.getUser().getId())
                .id(payment.getId())
                .build();
        return responseDto;
    }

    @Override
    public PaymentResponseDto create(PaymentRequestDto requestDto) {
        Optional<Payment> existingPayment = paymentRepository.findByCardNumber(requestDto.cardNumber());

        if(existingPayment.isPresent()){
            throw new EntityNotFoundException("This card already have!");
        }
        User user = userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User with thid is " + requestDto.userId() + " not found!"));
        Payment payment = Payment.builder()
                .cvv(requestDto.cvv())
                .cardNumber(requestDto.cardNumber())
                .expirationDate(requestDto.expirationDate())
                .user(user)
                .build();
        paymentRepository.save(payment);

        PaymentResponseDto responseDto = PaymentResponseDto.builder()
                .cvv(payment.getCvv())
                .expirationDate(payment.getExpirationDate())
                .cardNumber(payment.getCardNumber())
                .userID(payment.getUser().getId())
                .id(payment.getId())
                .build();

        return responseDto;
    }

    @Override
    public PaymentResponseDto update(long id, PaymentRequestDto requestDto) {
        Payment payment = getEntityById(id);

        payment.setCardNumber(requestDto.cardNumber());
        payment.setExpirationDate(requestDto.expirationDate());
        payment.setCvv(requestDto.cvv());

        if (!requestDto.userId().equals(payment.getUser().getId())) {
            User user = userRepository.findById(requestDto.userId())
                    .orElseThrow(() -> new EntityNotFoundException("User with id " + requestDto.userId() + " not found!"));
            payment.setUser(user);
        }

        payment = paymentRepository.save(payment);

        PaymentResponseDto responseDto = PaymentResponseDto.builder()
                .id(payment.getId())
                .userID(payment.getUser().getId())
                .cardNumber(payment.getCardNumber())
                .expirationDate(payment.getExpirationDate())
                .cvv(payment.getCvv())
                .build();

        return responseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Payment payment = getEntityById(id);
        paymentRepository.delete(payment);
    }

    @Override
    public Payment getEntityById(long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Payment with this id "+id +" not found!"));
    }
}
