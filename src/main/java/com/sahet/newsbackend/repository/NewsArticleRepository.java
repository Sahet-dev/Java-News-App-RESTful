package com.sahet.newsbackend.repository;

import com.sahet.newsbackend.model.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    List<NewsArticle> findByCategoryName(String name);

    @Query("SELECT n FROM NewsArticle n ORDER BY n.publishedAt DESC")
    List<NewsArticle> findAllByOrderByPublishedAtDesc();

    // Fetch articles by category name sorted by publishedAt in descending order
    List<NewsArticle> findByCategoryNameOrderByPublishedAtDesc(String name);
}

