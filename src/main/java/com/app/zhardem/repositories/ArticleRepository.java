package com.app.zhardem.repositories;

import com.app.zhardem.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a.id FROM Article a")
    List<Long> findAllIds();

    @Query("SELECT DISTINCT a.tags FROM Article a")
    List<String> findAllUniqueTags();

    @Query("SELECT a FROM Article a WHERE " +
            "LOWER(a.title) LIKE LOWER(CONCAT('%',:query,'%')) OR " +
            "LOWER(a.title ) LIKE LOWER(CONCAT('%',:query,'%'))")
    List<Article> findByTitleContainingOrContentContaining(@Param("query") String query);
}
