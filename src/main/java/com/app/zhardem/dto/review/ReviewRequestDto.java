package com.app.zhardem.dto.review;

public record ReviewRequestDto(
        Long userId,
         int rating,
       String reviewText,
        long doctorId
) { }
