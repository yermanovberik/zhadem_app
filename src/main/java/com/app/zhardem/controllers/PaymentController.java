package com.app.zhardem.controllers;

import com.app.zhardem.dto.payment.PaymentRequestDto;
import com.app.zhardem.dto.payment.PaymentResponseDto;
import com.app.zhardem.services.PaymentService;
import jakarta.validation.Valid;
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
