package com.sahet.newsbackend.controller;

import com.sahet.newsbackend.dto.NewsArticleRequest;
import com.sahet.newsbackend.service.NewsArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminNewsController {

    private final NewsArticleService articleService;

    public AdminNewsController(NewsArticleService articleService) {
        this.articleService = articleService;
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<String> createArticle(@RequestBody NewsArticleRequest request) {
        articleService.createArticle(request);
        return ResponseEntity.ok("Article created successfully!");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateArticle(@PathVariable Long id, @RequestBody NewsArticleRequest request) {
        articleService.updateArticle(id, request);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}
