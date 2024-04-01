package com.app.zhardem.controllers;


import com.app.zhardem.dto.review.ReviewRequestDto;
import com.app.zhardem.dto.review.ReviewResponseDto;
import com.app.zhardem.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.SameLen;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/review")
public class ReviewController {

    private final ReviewService reviewService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseDto> getAllReviews(){
        return reviewService.getAllReviews();
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewResponseDto getReviewById(@PathVariable long id) {
        return reviewService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseDto createReview(@RequestBody @Valid ReviewRequestDto requestDto) {
        return reviewService.create(requestDto);
    }

    @PutMapping("/{id}")
    public ReviewResponseDto updateReview(@PathVariable long id, @RequestBody @Valid ReviewRequestDto requestDto) {
        return reviewService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable long id) {
        reviewService.delete(id);
    }

    @GetMapping("/doctor/{doctorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseDto> getReviewOfDoctor(@PathVariable long doctorId){
        return reviewService.getReviewsOfDoctor(doctorId);
    }

}

