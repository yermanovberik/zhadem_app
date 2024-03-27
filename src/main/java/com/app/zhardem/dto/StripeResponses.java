package com.app.zhardem.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class StripeResponses<T> {
    private String status;
    private String message;
    private Integer httpStatus;
    private T data;

    public StripeResponses(String status, String message, Integer httpStatus, T data){
        this.status = status;
        this.message = message;
        this.httpStatus = httpStatus;
        this.data = data;
    }
}
