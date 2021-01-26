package com.example.blog.Tests;

import com.example.blog.controllers.RestPostController;
import static org.assertj.core.api.Assertions.*;

import com.example.blog.entities.Post;
import com.example.blog.entities.User;
import com.example.blog.repositories.PostRepository;
import com.example.blog.repositories.UserRepository;
import com.example.blog.service.PostService;
import org.hibernate.event.spi.PostCollectionRecreateEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/*
@RunWith(SpringRunner.class)
@DataJpaTest
public class ControllerTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService service;

    @Autowired
    private RestPostController controller;

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {

        User user = new User();
        user.setUsername("test");
        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByUsername(user.getUsername()).get();

        // then
        assertThat(found.getUsername())
                .isEqualTo(user.getUsername());

    }
}

 */
