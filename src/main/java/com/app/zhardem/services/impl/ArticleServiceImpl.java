package com.app.zhardem.services.impl;

import com.app.zhardem.dto.article.ArticleRequestDto;
import com.app.zhardem.dto.article.ArticleResponseDto;
import com.app.zhardem.exceptions.entity.EntityNotFoundException;
import com.app.zhardem.models.Article;
import com.app.zhardem.repositories.ArticleRepository;
import com.app.zhardem.services.ArticleService;
import com.app.zhardem.services.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final FileService fileService;
    private final Random random = new Random();

    @Override
    public ArticleResponseDto getById(long id) {
        Article article = getEntityById(id);

        ArticleResponseDto responseDto = ArticleResponseDto.builder()
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
        return null;
    }

    @Override
    public ArticleResponseDto createArticle(ArticleRequestDto requestDto) throws IOException {
        String imagePath = fileService.uploadFile(requestDto.file());

        Article article = Article.builder()
                .author(requestDto.author())
                .publicationDate(requestDto.publicationDate())
                .imagePath(imagePath)
                .tags(requestDto.tags())
                .title(requestDto.title())
                .build();

        articleRepository.save(article);


        ArticleResponseDto responseDto = ArticleResponseDto.builder()
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


    @Override
    public List<ArticleResponseDto> getTrendingArticles() {
        List<Long> ids = articleRepository.findAllIds();
        Collections.shuffle(ids, random);

        List<Long> randomIds = ids.stream().limit(5).collect(Collectors.toList());

        List<Article> randomArticles = articleRepository.findAllById(randomIds);

        List<ArticleResponseDto> responseDtos = new ArrayList<>();

        for (Article article : randomArticles) {
            String fileName= article.getImagePath();
            URL presignedUrl = fileService.generatePresignedUrl(fileName, 600);
            String url = presignedUrl.toString();
            ArticleResponseDto responseDto = ArticleResponseDto.builder()
                    .title(article.getTitle())
                    .publicationDate(article.getPublicationDate())
                    .author(article.getAuthor())
                    .tags(article.getTags())
                    .imagePath(url)
                    .build();
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }


    @Override
    public List<String> getPopularArticles() {
        List<String> allTags = articleRepository.findAllUniqueTags();

        Collections.shuffle(allTags, random);

        return allTags.stream().limit(10).collect(Collectors.toList());
    }

    @Override
    public List<ArticleResponseDto> getRelatedArticles() {
        List<Long> ids = articleRepository.findAllIds();
        Collections.shuffle(ids, random);

        List<Long> randomIds = ids.stream().limit(5).collect(Collectors.toList());

        List<Article> randomArticles = articleRepository.findAllById(randomIds);

        List<ArticleResponseDto> responseDtos = new ArrayList<>();

        for (Article article : randomArticles) {
            String fileName= article.getImagePath();
            URL presignedUrl = fileService.generatePresignedUrl(fileName, 600);
            String url = presignedUrl.toString();
            ArticleResponseDto responseDto = ArticleResponseDto.builder()
                    .title(article.getTitle())
                    .publicationDate(article.getPublicationDate())
                    .author(article.getAuthor())
                    .tags(article.getTags())
                    .imagePath(url)
                    .build();
            responseDtos.add(responseDto);
        }

        return responseDtos;
    }


    public List<ArticleResponseDto> searchByQuery(String query) {
        List<Article> articles = articleRepository.findByTitleContainingOrContentContaining(query);

        List<ArticleResponseDto> responseDtos = new ArrayList<>();
        for (Article article : articles) {
            String fileName= article.getImagePath();
            URL presignedUrl = fileService.generatePresignedUrl(fileName, 600);
            String url = presignedUrl.toString();
            ArticleResponseDto responseDto = ArticleResponseDto.builder()
                    .title(article.getTitle())
                    .publicationDate(article.getPublicationDate())
                    .author(article.getAuthor())
                    .tags(article.getTags())
                    .imagePath(url)
                    .build();
            responseDtos.add(responseDto);
        }
        return responseDtos;
    }
}