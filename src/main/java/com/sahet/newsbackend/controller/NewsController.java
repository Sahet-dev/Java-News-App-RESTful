package com.sahet.newsbackend.controller;

import com.sahet.newsbackend.dto.NewsArticleResponse;
import com.sahet.newsbackend.model.NewsArticle;
import com.sahet.newsbackend.service.NewsArticleService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsArticleService articleService;
    public NewsController(NewsArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsArticle> getArticleById(@PathVariable Long id) {
        NewsArticle article = articleService.getArticleById(id);
        if (article == null) {
            return ResponseEntity.notFound().build(); // Return 404
        }
        return ResponseEntity.ok(article);
    }


    @GetMapping("/all")
    @Cacheable(value = "articles", key = "'all'")
    public List<NewsArticleResponse> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/category/{category}")
    @Cacheable(value = "articles", key = "#category")
    public List<NewsArticle> getArticlesByCategory(@PathVariable String category) {
        return articleService.getArticlesByCategory(category);
    }
}