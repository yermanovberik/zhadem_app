package com.app.zhardem.dto;

import lombok.Data;

@Data
public class CreatePaymentRequest {
    private Long amount;
    private Long appointmentID;
    private Long doctorID;
    private Long userID;
}