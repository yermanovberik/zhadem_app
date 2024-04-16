package com.app.zhardem.dto;

import lombok.Data;

@Data
public class CreatePaymentRequest {
    private Long userID;
    private Long doctorID;
    private int dayNumber;
}