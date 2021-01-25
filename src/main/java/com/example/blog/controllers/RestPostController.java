package com.example.blog.controllers;


import com.example.blog.DTO.PostDTO;
import com.example.blog.entities.Post;
import com.example.blog.entities.User;
import com.example.blog.repositories.PostRepository;
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
import java.util.stream.Collectors;


@NoArgsConstructor
@RestController
@RequestMapping("posts/api")
public class RestPostController {

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/posts")
    public List<PostDTO> list()
    {
        return postService.getAllPosts()
                .stream()
                .map(postService::postToPostDTO)
                .collect(Collectors.toList());
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
    public ResponseEntity<Post> delete(@PathVariable Long id, Principal principal) throws NotFoundException {

        if(postService.deletePost(id, principal))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/posts/{username}")
    public List<PostDTO> getAuthorPostsInJson(@PathVariable String username) {

        return postService.getAllPosts()
                .stream()
                .map(postService::postToPostDTO)
                .filter(post -> post.getPost_authors()
                        .contains(username))
                .collect(Collectors.toList());

    }

    @GetMapping("/sort/title")
    public List<PostDTO> sortByTitle(@RequestParam String sort)
    {
        if(sort.equals("asc"))
             return postRepository.findByOrderByTitleAsc()
                    .stream()
                    .map(postService::postToPostDTO)
                    .collect(Collectors.toList());

        else if(sort.equals("dsc"))
            return postRepository.findByOrderByTitleDesc()
                    .stream()
                    .map(postService::postToPostDTO)
                    .collect(Collectors.toList());

        else
            return postService.getAllPosts()
                    .stream()
                    .map(postService::postToPostDTO)
                    .collect(Collectors.toList());

    }

   /* @GetMapping("/sort/postcontent")
    public List<PostDTO> sortByContent(@RequestParam String sort)
    {
        if(sort.equals("asc"))
            return postRepository.findByOrderByPost_contentAsc()
                    .stream()
                    .map(postService::postToPostDTO)
                    .collect(Collectors.toList());

        else if(sort.equals("dsc"))
            return postRepository.findByOrderByPost_contentDesc()
                    .stream()
                    .map(postService::postToPostDTO)
                    .collect(Collectors.toList());

        else
            return postService.getAllPosts()
                    .stream()
                    .map(postService::postToPostDTO)
                    .collect(Collectors.toList());
    }

    */
}

