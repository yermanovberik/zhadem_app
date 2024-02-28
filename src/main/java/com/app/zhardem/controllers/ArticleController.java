package com.app.zhardem.controllers;
import com.app.zhardem.dto.article.ArticleRequestDto;
import com.app.zhardem.dto.article.ArticleResponseDto;
import com.app.zhardem.services.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/article")
public class ArticleController {
    private final ArticleService articleService;
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ArticleResponseDto getArticleById(@PathVariable long id) {
        return articleService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponseDto creatArticle(@RequestBody @Valid ArticleRequestDto requestDto) {
        return articleService.create(requestDto);
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
}
