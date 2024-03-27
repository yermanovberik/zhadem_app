package com.app.zhardem.controllers;
import com.app.zhardem.dto.article.ArticleRequestDto;
import com.app.zhardem.dto.article.ArticleResponseDto;
import com.app.zhardem.services.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/article")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/trending")
    @ResponseStatus(HttpStatus.OK)
    public List<ArticleResponseDto> getTrendingArticles(){
        return articleService.getTrendingArticles();
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getPopularArticles(){
        return articleService.getPopularArticles();
    }

    @GetMapping("/related")
    @ResponseStatus(HttpStatus.OK)
    public List<ArticleResponseDto> getRelatedArticles(){
        return articleService.getRelatedArticles();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ArticleResponseDto getArticleById(@PathVariable long id) {
        return articleService.getById(id);
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createArticle(
            @RequestPart("title") String title,
            @RequestPart("author") String author,
            @RequestPart("publicationDate") String publicationDate,
            @RequestPart("tags") String tags,
            @RequestPart("file") MultipartFile file) {
        try {
            ArticleRequestDto requestDto = ArticleRequestDto.builder()
                    .publicationDate(LocalDateTime.parse(publicationDate))
                    .author(author)
                    .file(file)
                    .tags(tags)
                    .title(title)
                    .build();

            ArticleResponseDto responseDto = articleService.createArticle(requestDto);
            return ResponseEntity.ok(responseDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error while creating article: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ArticleResponseDto updateReview(@PathVariable long id, @RequestBody @Valid ArticleRequestDto requestDto) {
        return articleService.update(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable long id) {
        articleService.delete(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ArticleResponseDto>> searchArticles(@RequestParam String query) {
        List<ArticleResponseDto> articles = articleService.searchByQuery(query);
        return ResponseEntity.ok(articles);
    }
}
