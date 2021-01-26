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
import org.springframework.security.core.parameters.P;
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
    public ResponseEntity<PostDTO> get(@PathVariable Long id) {
        try {
            Post post = postService.getPost(id);
            return new ResponseEntity<PostDTO>(postService.postToPostDTO(post), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<PostDTO>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/post")
    public ResponseEntity<PostDTO> add(@RequestBody PostDTO postDTO, Principal principal) {
        if(principal==null)
            return new ResponseEntity<PostDTO>(HttpStatus.UNAUTHORIZED);
        postService.addPost(postDTO, principal);
            return new ResponseEntity<PostDTO>(postDTO, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@RequestBody PostDTO postDTO, @PathVariable Long id, Principal principal) {
        if(principal==null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if(postService.updatePost(id, postDTO, principal))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> delete(@PathVariable Long id, Principal principal) throws NotFoundException {

        if(principal==null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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

    @GetMapping(value = "/posts/filter/{keyword}")
    public List<PostDTO> getPostsByKeyword(@PathVariable String keyword) {

        return postService.filterPostsByKeyword(keyword);
    }


    @PatchMapping("/tag/{id}")
    public ResponseEntity<Post> addTag(@RequestBody String tag, @PathVariable Long id, Principal principal) throws NotFoundException {

        return postService.addTagToPost(id, tag, principal);
    }

}

