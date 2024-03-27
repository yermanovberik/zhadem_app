package com.app.zhardem.dto;

import lombok.Builder;

@Builder
public record CapturePaymentResponse(String sessionId, String sessionStatus, String paymentStatus) {}
