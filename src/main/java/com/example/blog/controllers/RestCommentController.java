package com.example.blog.controllers;

import com.example.blog.DTO.CommentDTO;
import com.example.blog.entities.Comment;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("comment/api")
public class RestCommentController {

    private PostService postService;
    private UserService userService;
    @Autowired
    private CommentService commentService;

    @PostMapping("/{id}")
    public ResponseEntity<Comment> addComment(@Valid @RequestBody CommentDTO commentDTO, @PathVariable Long id, Principal principal) throws NotFoundException {
        commentService.addComment(commentDTO, id, principal);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long postId, @PathVariable Long id) throws Exception {

        commentService.getComment(postId, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{postId}/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long postId, @PathVariable Long id, @Valid @RequestBody CommentDTO commentDTO,
                                                 Principal currentUser) throws Exception {

        Comment updatedComment = commentService.updateComment(postId, id, commentDTO, currentUser);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/{id}")
    public ResponseEntity<Comment> deleteComment(@PathVariable Long postId, @PathVariable Long id, Principal currentUser) throws Exception {

        commentService.deleteComment(postId, id, currentUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/comments/{username}")
    public List<CommentDTO> getUserComments(@PathVariable String username) {

        return commentService
                .findCommentsByUser(username)
                .stream()
                .map(commentService::commentToCommentDto)
                .collect(Collectors.toList());

    }

}
