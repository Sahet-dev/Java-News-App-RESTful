package com.sahet.newsbackend.controller;

import com.sahet.newsbackend.dto.NewsArticleRequest;
import com.sahet.newsbackend.service.NewsArticleService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> createArticle(@RequestBody NewsArticleRequest request) {
        try {
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Title is required.");
            }

            if (request.getContent() == null || request.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Content is required.");
            }

            if (request.getCategoryName() == null || request.getCategoryName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Category is required.");
            }

            articleService.createArticle(request);
            return ResponseEntity.ok("Article created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating article: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateArticle(@PathVariable Long id, @RequestBody NewsArticleRequest request) {
        System.out.println("Received update request for ID: " + id);
        System.out.println("Request Data: " + request); // Check if data is received

        if (request == null) {
            System.out.println("Request body is null!");
            return ResponseEntity.badRequest().build();
        }
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
