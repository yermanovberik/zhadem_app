package com.app.zhardem.dto.article;


import java.time.LocalDateTime;

public record ArticleRequestDto(
        String title,
        String author,
        LocalDateTime publicationDate,
        String category,
        String tags,
        String imagePath
) {}