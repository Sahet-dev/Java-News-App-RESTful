package com.sahet.newsbackend.service;

import com.sahet.newsbackend.dto.NewsArticleRequest;
import com.sahet.newsbackend.dto.NewsArticleResponse;
import com.sahet.newsbackend.model.NewsArticle;
import com.sahet.newsbackend.model.NewsCategory;
import com.sahet.newsbackend.repository.NewsArticleRepository;
import com.sahet.newsbackend.repository.NewsCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
    public List<NewsArticleResponse> getAllArticles() {
        return articleRepository.findAll()
                .stream()
                .sorted((a, b) -> b.getPublishedAt().compareTo(a.getPublishedAt())) // Sort by newest
                .limit(100)
                .map(article -> new NewsArticleResponse(
                        article.getId(),
                        article.getTitle(),
                        article.getContent(),
                        article.getImageUrl(),
                        article.getCategory().getName(),
                        article.getPublishedAt()
                ))
                .toList();
    }

//    @CacheEvict(value = "articles", key = "'all'", beforeInvocation = true)
//    public void createArticle(NewsArticleRequest request) {
//        NewsCategory category = categoryRepository.findByName(request.getCategoryName())
//                .orElseGet(() -> {
//                    NewsCategory newCategory = new NewsCategory(request.getCategoryName());
//                    return categoryRepository.save(newCategory);
//                });
//
//        NewsArticle article = new NewsArticle();
//        article.setTitle(request.getTitle());
//        article.setContent(request.getContent());
//        article.setImageUrl(request.getImageUrl());
//        article.setPublishedAt(request.getPublishedAt() != null ? request.getPublishedAt() : LocalDateTime.now());
//        article.setCategory(category);
//
//        articleRepository.save(article);
//    }

    @CacheEvict(value = "articles", key = "'all'", beforeInvocation = true)
    public void createArticle(NewsArticleRequest request) {
        try {
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
        } catch (Exception e) {
            throw new RuntimeException("Failed to create article: " + e.getMessage());
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "articles", key = "'all'", beforeInvocation = true),
            @CacheEvict(value = "articles", key = "#request.getCategoryName()", beforeInvocation = true)
    })
    @Transactional
    public void updateArticle(Long articleId, NewsArticleRequest request) {
        NewsArticle article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("Article not found with ID: " + articleId));

        if (request.getTitle() != null && !request.getTitle().isEmpty()) {
            article.setTitle(request.getTitle());
        }

        if (request.getContent() != null && !request.getContent().isEmpty()) {
            article.setContent(request.getContent());
        }

        if (request.getImageUrl() != null && !request.getImageUrl().isEmpty()) {
            article.setImageUrl(request.getImageUrl());
        }

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

    public NewsArticleResponse getArticleById(Long id) {
        NewsArticle article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return null;
        }

        // Convert entity to DTO
        return new NewsArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getImageUrl(),
                article.getCategory().getName(), // Extract category name
                article.getPublishedAt()
        );
    }


    @CacheEvict(value = "articles", allEntries = true)
    public void deleteArticle(Long id) {
        NewsArticle article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found with id: " + id));
        articleRepository.delete(article);
    }

}
