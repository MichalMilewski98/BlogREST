package com.example.blog.Tests;

import com.example.blog.controllers.RestPostController;
import static org.assertj.core.api.Assertions.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ControllerTests {

    @Autowired
    private RestPostController controller;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
}
