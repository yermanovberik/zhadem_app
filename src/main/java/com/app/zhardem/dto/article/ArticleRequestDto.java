package com.app.zhardem.dto.article;


import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Builder
public record ArticleRequestDto(
        String title,
        String author,
        LocalDateTime publicationDate,
        String tags,
        MultipartFile file
) {}