package com.acme.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

import static com.acme.app.ArticleFactory.newArticle;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private ArticleRepository articleRepository;

    @PostConstruct
    public void initialize() {
        articleRepository.save(newArticle("Hello world !"));
        articleRepository.save(newArticle("Lorem ipsum dolor sit amet consectetur adipiscing"));
        articleRepository.save(newArticle("Foo Bar Power"));
    }

}