package com.app.zhardem.dto.article;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ArticleResponseDto(
        String title,
        String author,
        LocalDateTime publicationDate,
        String tags,
        String imagePath
) {}