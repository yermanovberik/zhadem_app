package com.app.zhardem.dto;

import lombok.Builder;

@Builder
public record PaymentTotal(
    double injection,
    double drip,
    double total

) {
}
