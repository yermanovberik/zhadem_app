package com.app.zhardem.dto.article;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleResponseDto(
        Long id,
        String title,
        String author,
        LocalDateTime publicationDate,
        String category,
        String tags,
        String imagePath
) {}