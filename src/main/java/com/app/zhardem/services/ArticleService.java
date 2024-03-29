package com.app.zhardem.services;

import com.app.zhardem.dto.article.ArticleRequestDto;
import com.app.zhardem.dto.article.ArticleResponseDto;
import com.app.zhardem.models.Article;

import java.util.List;

public interface ArticleService extends CrudService<Article, ArticleRequestDto, ArticleResponseDto> {
    List<ArticleResponseDto> getTrendingArticles();
    List<String> getPopularArticles();
    List<ArticleResponseDto> getRelatedArticles();

    ArticleResponseDto createArticle(ArticleRequestDto requestDto) throws Exception;

     List<ArticleResponseDto> searchByQuery(String query);

}
