package com.app.zhardem.services.impl;

import com.app.zhardem.dto.review.ReviewRequestDto;
import com.app.zhardem.dto.review.ReviewResponseDto;
import com.app.zhardem.models.Review;
import com.app.zhardem.models.User;
import com.app.zhardem.repositories.ReviewRepository;
import com.app.zhardem.repositories.UserRepository;
import com.app.zhardem.services.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponseDto getById(long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new com.app.zhardem.exceptions.entity.EntityNotFoundException("Review with id "+ id + " not found."));
        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .userId(id)
                .rating(review.getRating())
                .reviewText(review.getReviewText()).build();
        return responseDto;
    }

    @Override
    public ReviewResponseDto create(ReviewRequestDto requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = " ";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        log.info(username);
        User user = userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User with id this "+ requestDto.userId() + "not ofund!"));
        Review review = Review.builder()
                .reviewText(requestDto.reviewText())
                .user(user)
                .rating(requestDto.rating())
                .build();
        reviewRepository.save(review);

        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .name(user.getFullName())
                .userId(requestDto.userId())
                .rating(review.getRating())
                .reviewText(review.getReviewText()).build();

        return responseDto;
    }

    @Override
    @Transactional
    public ReviewResponseDto update(long id, ReviewRequestDto requestDto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        User user = userRepository.findById(requestDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("User with id this "+ requestDto.userId() + "not ofund!"));


        review.setRating(requestDto.rating());
        review.setReviewText(requestDto.reviewText());
        review.setUser(user);

        Review updatedReview = reviewRepository.save(review);


        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .userId(requestDto.userId())
                .rating(review.getRating())
                .reviewText(review.getReviewText()).build();

        return responseDto;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        reviewRepository.delete(review);
    }

    @Override
    public Review getEntityById(long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
    }

    public List<ReviewResponseDto> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();

        List<ReviewResponseDto> responseDto = reviews.stream()
                .map(review -> new ReviewResponseDto(
                        review.getUser().getFullName(),
                        review.getUser().getId(),
                        review.getRating(),
                        review.getReviewText()
                ))
                .collect(Collectors.toList());

        return responseDto;
    }




}
