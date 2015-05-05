package com.acme.app;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MockMvcUnitTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleResource articleResource;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(articleResource).build();

        List<Article> articles = new ArrayList<>();
        articles.add(ArticleFactory.newArticle("Article 1"));
        articles.add(ArticleFactory.newArticle("Article 2"));
        articles.add(ArticleFactory.newArticle("Article 3"));
        Mockito.when(articleRepository.findAll()).thenReturn(articles);
    }

    @Test
    public void testGetArticles() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
    }

}
