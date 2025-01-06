package com.sahet.newsbackend.controller;

import com.sahet.newsbackend.dto.NewsArticleRequest;
import com.sahet.newsbackend.dto.NewsArticleResponse;
import com.sahet.newsbackend.model.NewsArticle;
import com.sahet.newsbackend.service.NewsArticleService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class NewsAdminController {

    private final NewsArticleService articleService;
    public NewsAdminController(NewsArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> createArticle(@RequestBody NewsArticleRequest request) {
        articleService.createArticle(request);
        return ResponseEntity.ok("Article created successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateArticle(@PathVariable Long id, @RequestBody NewsArticleRequest request) {
        articleService.updateArticle(id, request);
        return ResponseEntity.noContent().build();
    }
}