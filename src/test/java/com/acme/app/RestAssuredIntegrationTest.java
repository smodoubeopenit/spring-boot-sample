package com.acme.app;

import com.jayway.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:0")
public class RestAssuredIntegrationTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestAssuredIntegrationTest.class);

    @Value("${local.server.port}")
    private int port;

    @Test
    public void testApplication() {
        LOGGER.info("L'application tourne sur le port : " + port);
    }

    @Test
    public void testGetArticles() {
        when()
                .get("http://localhost:" + port + "/articles").
        then()
                .body(containsString("Hello world !"))
                .body(containsString("Lorem ipsum dolor sit amet consectetur adipiscing"))
                .body(containsString("Foo Bar Power"))
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON).
        extract()
                .response()
                .asString();
    }


}
