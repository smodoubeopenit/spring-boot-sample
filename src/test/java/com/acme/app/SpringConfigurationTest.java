package com.acme.app;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class SpringConfigurationTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void testDependencyInjection() {
        Assertions.assertThat(articleRepository).isNotNull();
    }

    @Test
    public void testInitialize() {
        List<Article> articles = articleRepository.findAll();
        Assertions.assertThat(articles).isNotNull().hasSize(3);
    }

}