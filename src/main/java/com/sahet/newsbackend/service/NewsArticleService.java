package com.sahet.newsbackend.service;

import com.sahet.newsbackend.dto.NewsArticleRequest;
import com.sahet.newsbackend.model.NewsArticle;
import com.sahet.newsbackend.model.NewsCategory;
import com.sahet.newsbackend.repository.NewsArticleRepository;
import com.sahet.newsbackend.repository.NewsCategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsArticleService {
    private final NewsArticleRepository articleRepository;
    private final NewsCategoryRepository categoryRepository;
    public NewsArticleService(NewsArticleRepository articleRepository, NewsCategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }



    // Cache up to 20 articles for each category
    @Cacheable(value = "articles", key = "#categoryName")
    public List<NewsArticle> getArticlesByCategory(String categoryName) {
        return articleRepository.findByCategoryName(categoryName)
                .stream()
                .limit(20)
                .toList();
    }

    @Cacheable(value = "articles", key = "'all'")
    public List<NewsArticle> getAllArticles() {
        return articleRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getPublishedAt().compareTo(a.getPublishedAt())) // Sort by newest
                .limit(100) // Optional: Limit to the latest 100 articles
                .toList();
    }

    public void createArticle(NewsArticleRequest request) {
        NewsCategory category = categoryRepository.findByName(request.getCategoryName())
                .orElseGet(() -> {
                    NewsCategory newCategory = new NewsCategory(request.getCategoryName());
                    return categoryRepository.save(newCategory);
                });

        NewsArticle article = new NewsArticle();
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setImageUrl(request.getImageUrl());
        article.setPublishedAt(request.getPublishedAt() != null ? request.getPublishedAt() : LocalDateTime.now());
        article.setCategory(category);

        articleRepository.save(article);
    }

    @Transactional
    public void updateArticle(Long articleId, NewsArticleRequest request) {
        // Fetch the existing article
        NewsArticle article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));

        // Update title if provided
        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            article.setTitle(request.getTitle());
        }

        // Update content if provided
        if (request.getContent() != null && !request.getContent().isEmpty()) {
            article.setContent(request.getContent());
        }

        // Update image URL if provided
        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            article.setImageUrl(request.getImageUrl());
        }

        // Update published date if provided
        if (request.getPublishedAt() != null) {
            article.setPublishedAt(request.getPublishedAt());
        }

        // Update category if provided
        if (request.getCategoryName() != null && !request.getCategoryName().isEmpty()) {
            NewsCategory category = categoryRepository.findByName(request.getCategoryName())
                    .orElseGet(() -> categoryRepository.save(new NewsCategory(request.getCategoryName())));
            article.setCategory(category);
        }

        // Save the updated article (not strictly necessary with @Transactional)
        articleRepository.save(article);
    }

    public NewsArticle getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Article with ID " + id + " not found."));
    }
}
