package com.app.zhardem.services;

import com.app.zhardem.dto.payment.PaymentRequestDto;
import com.app.zhardem.dto.payment.PaymentResponseDto;
import com.app.zhardem.models.Payment;

public interface PaymentService extends CrudService<Payment, PaymentRequestDto, PaymentResponseDto> {
}
