package com.acme.app;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationTest {

    private final static Logger LOGGER = getLogger(ApplicationTest.class);

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleResource articleResource;

    @Test
    public void testDependencyInjection() {
        Assertions.assertThat(articleRepository).isNotNull();
        Assertions.assertThat(articleResource).isNotNull();
    }

    @Test
    public void testInitialize() {
        List<Article> articles = articleResource.getArticles();
        Assertions.assertThat(articles).isNotNull().hasSize(3);
    }

}
