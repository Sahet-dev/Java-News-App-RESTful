package com.sahet.newsbackend.repository;

import com.sahet.newsbackend.model.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {
    Optional<NewsCategory> findByName(String name);
}
