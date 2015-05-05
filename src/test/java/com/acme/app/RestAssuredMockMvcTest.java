package com.acme.app;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.when;
import static org.hamcrest.CoreMatchers.containsString;

public class RestAssuredMockMvcTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleResource articleResource;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        RestAssuredMockMvc.standaloneSetup(articleResource);

        List<Article> articles = new ArrayList<>();
        articles.add(ArticleFactory.newArticle("Article 1"));
        articles.add(ArticleFactory.newArticle("Article 2"));
        articles.add(ArticleFactory.newArticle("Article 3"));
        Mockito.when(articleRepository.findAll()).thenReturn(articles);
    }

    @Test
    public void testGetArticles() throws Exception {
        when()
                .get("/articles").
        then()
                .body(containsString("Article 1"))
                .body(containsString("Article 2"))
                .body(containsString("Article 3"))
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON).
        extract()
                .response()
                .getMockHttpServletResponse()
                .getContentAsString();
    }


}
