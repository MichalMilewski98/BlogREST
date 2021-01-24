package com.example.blog.service;

import com.example.blog.DTO.CommentUserDTO;
import com.example.blog.DTO.PostDTO;
import com.example.blog.entities.Post;
import com.example.blog.entities.Tag;
import com.example.blog.entities.User;
import com.example.blog.entities.exception.PostNotFoundException;
import com.example.blog.repositories.PostRepository;
import com.example.blog.repositories.TagRepository;
import com.example.blog.repositories.UserRepository;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.EntityResponse;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.*;


@NoArgsConstructor
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserService userService;

    public List<Post> getAllPosts()
    {
        return postRepository.findAll();
    }

    public void insert(Post post)
    {
        postRepository.save(post);
    }

    public void save(List<Post> posts) {postRepository.saveAll(posts);}

    public Post getPost(Long id) {return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));}

    public void delete(Long id) {postRepository.deleteById(id);}

    public List<Post> getUserPosts(User user)
    {
        List<Post> userPosts = new ArrayList<>();
        List<Post> posts = user.getPosts();
        for (Post post : posts)
        {
            userPosts.add(post);
        }
        return userPosts;
    }

    public List<Post> getPublicPosts()
    {
        List<Post> userPosts = new ArrayList<>();
        for (Post post : postRepository.findAll())
        {
            if(!post.isIsprivate())
                userPosts.add(post);
        }
        return userPosts;
    }

    public List<Post> findByKeyword(String keyword){return postRepository.findByKeyword(keyword);}

    public List<Tag> tagList(String tag_name)
    {
        List<String> listOfStrings = Arrays.asList(tag_name.split("[ ,]+"));
        List<Tag> tags = new ArrayList<>();

        for (String _tag : listOfStrings) {
            if (!tagRepository.findByName(_tag).isPresent()) {
                Tag tag = new Tag(_tag);
                tagRepository.save(tag);
                tags.add(tag);
            } else {
                tags.add(tagRepository.findByName(_tag).get());
            }

        }
        return tags;
    }

    public List<User> authorList(String author)
    {
        List<String> listOfStrings = Arrays.asList(author.split("[ ,]+"));
        List<User> users = new ArrayList<>();
        for (String autor : listOfStrings)
        {
            Optional<User> user = userRepository.findByUsername(autor);
            users.add(user.get());
        }
        return users;
    }

    public String usernamesList(List<User> users)
    {
        String usernames ="";
        for (User user : users)
        {
            usernames += user.getUsername() + ",";
        }

        return usernames;
    }

    public String tagsList(List<Tag> tags)
    {
        String tag_names ="";
        for (Tag tag : tags)
        {
            tag_names += tag.getName() + ",";
        }

        return tag_names;
    }

    public List<Post> sortPosts(String sortField)
    {
        Sort sort = Sort.by(sortField).ascending();
        return postRepository.findAll(sort);
    }

    public Post postDTOtoPost(PostDTO postDTO)
    {
        Post post = new Post(postDTO.getId(), authorList(postDTO.getPost_authors()), postDTO.getTitle(), postDTO.getPost_content(), tagList(postDTO.getTag()), postDTO.isIsprivate());
        return post;
    }

    public PostDTO postToPostDTO(Post post)
    {
        PostDTO postDTO = new PostDTO(post.getId(), post.getTitle(), post.getPost_content(), usernamesList(post.getPost_authors()), tagsList(post.getPost_tags()), post.isIsprivate());
                return postDTO;
    }

    public Post addPost(PostDTO postDTO, Principal principal) {

        //User user = userRepository.findByUsername(currentUser.getName()).get();
        postDTO.setPost_authors(principal.getName());
        Post post = postDTOtoPost(postDTO);
        Post newPost = postRepository.save(post);

        return newPost;
    }

    public ResponseEntity<Post> updatePost(Long id, PostDTO postDTO, Principal principal) throws NotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));

        if (userService.isAuthor(post.getPost_authors(), principal.getName()))
        {
           post = postDTOtoPost(postDTO);
           postRepository.save(post);
           return new ResponseEntity<Post>(HttpStatus.OK);
        }
        return new ResponseEntity<Post>(HttpStatus.FORBIDDEN);
    }


    public ResponseEntity<Post> deletePost(Long id, Principal principal) throws NotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));
        if (userService.isAuthor(post.getPost_authors(), principal.getName()))
        {
            postRepository.deleteById(id);
            return new ResponseEntity<Post>(HttpStatus.OK);
        }
        return new ResponseEntity<Post>(HttpStatus.FORBIDDEN);
    }
}
