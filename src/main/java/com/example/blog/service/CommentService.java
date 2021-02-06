package com.example.blog.service;

import com.example.blog.DTO.CommentDTO;
import com.example.blog.DTO.CommentUserDTO;
import com.example.blog.entities.Comment;
import com.example.blog.entities.Post;
import com.example.blog.entities.User;
import com.example.blog.entities.exception.PostNotFoundException;
import com.example.blog.repositories.CommentRepository;
import com.example.blog.repositories.PostRepository;
import com.example.blog.repositories.UserRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Log
@AllArgsConstructor
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private PostService postService;
    @Autowired
    private UserService userService;

    public Comment save(Comment comment) {
        return commentRepository.saveAndFlush(comment);
    }

    public Comment getComment(Long id) {return commentRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));}

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public List<Comment> findAllComments(){return commentRepository.findAll();}

    public List<Comment> findCommentsByUser(String username)
    {

        return commentRepository.findCommentsByUser(userRepository.findByUsername(username).get());
    }

    public User getCommentAuthor(String author)
    {
        Optional<User> user = userRepository.findByUsername(author);
        return user.get();
    }

    public Comment commentDtoToComment(CommentDTO commentDTO)
    {
        Comment comment = new Comment(commentDTO.getId(), commentDTO.getBody(), getCommentAuthor(commentDTO.getUser()), postService.getPost(commentDTO.getPost_id()));
        return comment;
    }

    public CommentDTO commentToCommentDto(Comment comment)
    {
        CommentDTO commentDTO = new CommentDTO(comment.getId(), comment.getBody(), comment.getUser().getUsername(), comment.getPost().getId());
        return commentDTO;
    }

    public Comment commentUserDTOtoComment(CommentUserDTO commentUserDTO)
    {
        Comment comment = new Comment(commentUserDTO.getId(), commentUserDTO.getBody(), getCommentAuthor(commentUserDTO.getUser()), postService.getPost(commentUserDTO.getPost_id()));
        return comment;
    }


    public Comment addComment(CommentDTO commentDTO, Long postId, Principal principal) throws NotFoundException {
        
            User user = userRepository.findByUsername(principal.getName()).get();
            log.severe(user.getUsername());
            commentDTO.setUser(user.getUsername());
            commentDTO.setPost_id(postId);
            Comment comment = commentDtoToComment(commentDTO);
            return commentRepository.save(comment);
    }


    public Comment getComment(Long postId, Long id) throws Exception {

        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException(postId.toString()));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));

        if (comment.getPost().getId().equals(post.getId())) {
            return comment;
        }

        throw new Exception(String.valueOf(HttpStatus.BAD_REQUEST));
    }


    public boolean updateComment(Long id, CommentDTO commentDTO, Principal principal) throws Exception {

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));
        String username = principal.getName();
        User user = userService.getUser(username).get();

        if(commentRepository.findById(id).isPresent()) {

            if (comment.getUser().getId().equals(user.getId())) {
                commentDTO.setUser(comment.getUser().getUsername());
                comment = commentDtoToComment(commentDTO);
                commentRepository.save(comment);
                return true;
            }

            return false;
        }
        return false;
    }

    public ResponseEntity<Comment> deleteComment(Long postId, Long id, Principal principal) throws Exception{
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException(postId.toString()));
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));

        if (!comment.getPost().getId().equals(post.getId())) {
            return new ResponseEntity<Comment>(HttpStatus.NOT_FOUND);
        }

        String username = principal.getName();
        User user = userService.getUser(username).get();

        if (comment.getUser().getId().equals(user.getId()) || userService.findAdmin(principal)) {
            commentRepository.deleteById(comment.getId());
            return new ResponseEntity<Comment>(HttpStatus.OK);
        }

        return new ResponseEntity<Comment>(HttpStatus.UNAUTHORIZED);
    }

}
