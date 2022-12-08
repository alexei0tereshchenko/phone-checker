package org.example.controller;

import org.example.repository.AbstractRepositoryTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@ContextConfiguration(initializers = AbstractRepositoryTest.DockerMysqlDataSourceInitializer.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractControllerTest {

    protected MockMvc mvc;

    protected final HttpHeaders headers = new HttpHeaders();
}

