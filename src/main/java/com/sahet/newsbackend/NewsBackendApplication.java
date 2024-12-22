package com.sahet.newsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NewsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsBackendApplication.class, args);
    }

}
