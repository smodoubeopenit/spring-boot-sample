package com.acme.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest("server.port:0")
public class SpringIntegrationTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringIntegrationTest.class);

    @Value("${local.server.port}")
    private int port;

    @Test
    public void testApplication() {
        LOGGER.info("L'application tourne sur le port : " + port);
    }

}
