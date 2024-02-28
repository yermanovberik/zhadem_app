package com.app.zhardem.services;

import com.app.zhardem.dto.review.ReviewRequestDto;
import com.app.zhardem.dto.review.ReviewResponseDto;
import com.app.zhardem.models.Review;

import javax.print.DocFlavor;
import java.util.List;

public interface ReviewService extends CrudService<Review, ReviewRequestDto, ReviewResponseDto> {
    List<ReviewResponseDto> getAllReviews();
}
