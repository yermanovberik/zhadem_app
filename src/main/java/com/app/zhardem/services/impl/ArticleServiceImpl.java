package com.app.zhardem.services.impl;

import com.app.zhardem.dto.article.ArticleRequestDto;
import com.app.zhardem.dto.article.ArticleResponseDto;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.Article;
import com.app.zhardem.repositories.ArticleRepository;
import com.app.zhardem.services.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    @Override
    public ArticleResponseDto getById(long id) {
        Article article = getEntityById(id);

        ArticleResponseDto responseDto = ArticleResponseDto.builder()
                .id(article.getId())
                .author(article.getAuthor())
                .tags(article.getTags())
                .title(article.getTitle())
                .publicationDate(article.getPublicationDate())
                .imagePath(article.getImagePath())
                .title(article.getTitle()).build();

        return responseDto;
    }

    @Override
    public ArticleResponseDto create(ArticleRequestDto requestDto) {
        Article article = Article.builder()
                .author(requestDto.author())
                .tags(requestDto.tags())
                .title(requestDto.title())
                .publicationDate(requestDto.publicationDate())
                .imagePath(requestDto.imagePath())
                .title(requestDto.title()).build();

        articleRepository.save(article);


        ArticleResponseDto responseDto = ArticleResponseDto.builder()
                .id(article.getId())
                .author(article.getAuthor())
                .tags(article.getTags())
                .title(article.getTitle())
                .publicationDate(article.getPublicationDate())
                .imagePath(article.getImagePath())
                .title(article.getTitle()).build();

        return responseDto;
    }

    @Override
    public ArticleResponseDto update(long id, ArticleRequestDto requestDto) {
        return null;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Article article = getEntityById(id);

        articleRepository.delete(article);
    }

    @Override
    public Article getEntityById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article with id " + id + " not found!"));
    }


}