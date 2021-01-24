package com.example.blog.controllers;


import com.example.blog.DTO.PostDTO;
import com.example.blog.entities.Post;
import com.example.blog.entities.User;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@NoArgsConstructor
@RestController
@RequestMapping("posts/api")
public class RestPostController {

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public List<Post> list() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> get(@PathVariable Long id) {
        try {
            Post post = postService.getPost(id);
            return new ResponseEntity<Post>(post, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<Post>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/post")
    public void add(@RequestBody PostDTO postDTO, Principal principal) {
        postService.addPost(postDTO, principal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@RequestBody PostDTO postDTO, @PathVariable Long id, Principal principal) {
        try {
           postService.updatePost(id, postDTO, principal);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException | NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Principal principal) throws NotFoundException {

        postService.deletePost(id, principal);
    }

    @GetMapping(value = "/posts/{username}")
    public List<PostDTO> getAuthorPostsInJson(@PathVariable String username) {

        List<PostDTO> authorPosts = new ArrayList<>();
        List<Post> allPosts =  postService.getAllPosts();
        for (Post post : allPosts)
        {
            PostDTO postDTO = postService.postToPostDTO(post);
            if(postDTO.getPost_authors().contains(username))
                authorPosts.add(postDTO);
        }
        return authorPosts;
    }
}

