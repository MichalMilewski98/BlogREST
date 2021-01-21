package com.example.blog.controllers;

import com.example.blog.DTO.PostDTO;
import com.example.blog.entities.Post;
import com.example.blog.entities.User;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Log
@Controller
@AllArgsConstructor
public class BlogController {

    private PostService postService;
    private UserService userService;


    @GetMapping(value = "/posts")
    public List<Post> posts()
    {
        return postService.getAllPosts();
    }

    @GetMapping(value = "/delete/{id}")
    public List <Post> remove(@PathVariable Long id)
    {
        postService.delete(id);
        return postService.getAllPosts();
    }

    @GetMapping("/user_posts")
    public String getUserHomepage(Model model, String keyword, Principal principal)
    {
        String username="";
        if(principal.getName()!= null)
        {
            username = principal.getName();
        }
        User postAuthor = userService.getUser(username).get();
        List<Post> posts = postService.getUserPosts(postAuthor);
        List<PostDTO> postsDTO = new ArrayList<>();
        for (Post post : posts)
        {
            postsDTO.add(postService.postToPostDTO(post));
        }
        List<Post> filteredPosts = postService.findByKeyword(keyword);
        List<PostDTO> filteredPostsDTO = new ArrayList<>();
        for (Post post : filteredPosts)
        {
            filteredPostsDTO.add(postService.postToPostDTO(post));
        }

        if(keyword!=null)
            model.addAttribute("posts", filteredPostsDTO);
        else
            model.addAttribute("posts", postsDTO);

        return "index";
    }

    @GetMapping("/")
    public String getHomepage(Model model, String keyword)
    {
        List<Post> posts = postService.getPublicPosts();
        List<PostDTO> postsDTO = new ArrayList<>();
        for (Post post : posts)
        {
            postsDTO.add(postService.postToPostDTO(post));
        }
        List<Post> filteredPosts = postService.findByKeyword(keyword);
        List<PostDTO> filteredPostsDTO = new ArrayList<>();
        for (Post post : filteredPosts)
        {
            filteredPostsDTO.add(postService.postToPostDTO(post));
        }

        if(keyword!=null)
            model.addAttribute("posts", filteredPostsDTO);
        else
            model.addAttribute("posts", postsDTO);

        return "index";
    }

    @GetMapping("/sort")
    public String sortPosts(Model model, @RequestParam("sortField") String sortField)
    {
        List<Post> posts = postService.sortPosts(sortField);
        model.addAttribute("sortField", sortField);
        model.addAttribute("posts", posts);

        return "index";
    }

    @PostMapping(value = "/new_post")
    public String publishPost(@Valid @ModelAttribute PostDTO postDTO, BindingResult bindingResult, Principal principal)
    {
        if (bindingResult.hasErrors()) {
            return "new_post";
        }

        String username = principal.getName();
        Optional<User> optionaluser = userService.getUser(username);
        postDTO.setPost_authors(optionaluser.get().getUsername());
        Post post = postService.postDTOtoPost(postDTO);
        postService.insert(post);

        return "redirect:/";
    }

    @GetMapping(value = "/new_post")
    public String publishPost(Model model, Principal principal)
    {
        String username="";
        if(principal != null) {
            username = principal.getName();
        }

        Optional<User> optionaluser = userService.getUser(username);

        if (optionaluser.isPresent()) {
            PostDTO postDTO = new PostDTO();
            model.addAttribute("post", postDTO);
            return "new_post";
        }
        else
        {
            return "index";
        }
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model)
    {
        log.severe("GET POST!!!" + id);
        Post currentPost = postService.getPost(id);

            model.addAttribute("post", currentPost);
            return "post";
    }

    @GetMapping("/delete_post/{id}")
    public String deletePost(@PathVariable Long id, Principal principal)
    {
        boolean check = false;
        Post post = postService.getPost(id);
        for (User user : post.getPost_authors())
        {
            if(user.getUsername().equals(principal.getName()))
            {
                check = true;
                break;
            }
        }

        if(check || userService.findAdmin(principal))
        {
            postService.delete(id);
            return "redirect:/";
        }
        return "redirect:/post/" + id;
    }

    @GetMapping("edit_post/{id}")
    public String getEditPost(@PathVariable Long id, Model model, Principal principal)
    {
        boolean check=false;
        String username="";
        if(principal != null) {
            username = principal.getName();
        }
        Post currentPost = postService.getPost(id);
        Optional<User> optionaluser = userService.getUser(username);

        if (optionaluser.isPresent()) {
            for (User user : currentPost.getPost_authors())
            {
                if(user.getUsername().equals(principal.getName()))
                {
                    check = true;
                    break;
                }
            }
            if(check || userService.findAdmin(principal)) {
                PostDTO postDTO = postService.postToPostDTO(currentPost);
                model.addAttribute("post", postDTO);
                return "edit_post";
            }
        }
        else
        {
            model.addAttribute("errorData", true);
            return "redirect:/post/" + currentPost.getId();
        }
        model.addAttribute("errorData", true);
        return "redirect:/post/" + currentPost.getId();
    }

    @PostMapping("update_post/{id}")
    public String editPost(@ModelAttribute PostDTO postDTO)
    {
        Post post = postService.postDTOtoPost(postDTO);
        postService.insert(post);

        return "redirect:/";
    }

}
