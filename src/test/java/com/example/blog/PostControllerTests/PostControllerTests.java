package com.example.blog.PostControllerTests;


import com.example.blog.controllers.RestPostController;
import com.example.blog.entities.Post;
import com.example.blog.repositories.PostRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.sun.mail.imap.SortTerm.ARRIVAL;
import static java.util.Collections.singletonList;
import static javax.xml.transform.OutputKeys.VERSION;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RestPostController.class)
public class PostControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RestPostController restPostController;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenFindAll() {
        //given
        Post post = new Post();
        post.setPost_content("post_content");
        entityManager.persist(post);
        entityManager.flush();

        Post secondPost = new Post();
        secondPost.setPost_content("post_content_2");
        entityManager.persist(secondPost);
        entityManager.flush();

        //when
        List<Post> posts = postRepository.findAll();

        //then
        assertEquals(posts.size(),9);
        assertEquals(posts.get(7), post);
        assertEquals(posts.get(8),secondPost);
    }


  /*  @Test
    public void jsonTest()
    {
        // Given
        String jsonMimeType = "application/json";
        HttpUriRequest request = new HttpGet( "https://api.github.com/users/eugenp" );

        // When
        HttpResponse response = HttpClientBuilder.create().build().execute( request );

        // Then
        String mimeType = ContentType.getOrDefault(response.getEntity()).getMimeType();
        assertEquals( jsonMimeType, mimeType );
    }

   */
}
