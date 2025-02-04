package com.sahet.newsbackend.controller;

import com.sahet.newsbackend.dto.NewsArticleRequest;
import com.sahet.newsbackend.dto.NewsArticleResponse;
import com.sahet.newsbackend.model.NewsArticle;
import com.sahet.newsbackend.service.NewsArticleService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<NewsArticleResponse> getArticleById(@PathVariable Long id) {
        NewsArticleResponse articleResponse = articleService.getArticleById(id);
        if (articleResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(articleResponse);
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