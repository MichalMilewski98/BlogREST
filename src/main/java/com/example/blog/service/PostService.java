package com.example.blog.service;

import com.example.blog.DTO.PostDTO;
import com.example.blog.entities.Post;
import com.example.blog.entities.Tag;
import com.example.blog.entities.User;
import com.example.blog.entities.exception.PostNotFoundException;
import com.example.blog.repositories.PostRepository;
import com.example.blog.repositories.TagRepository;
import com.example.blog.repositories.UserRepository;
import javassist.NotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

    public List<Tag> tagList(String tag_name, Long id)
    {
        Optional<Post> currentPost = postRepository.findById(id);
        if(currentPost.isPresent()) {
            List<Tag> usedTags = currentPost.get().getPost_tags();

            for (Tag tag : usedTags) {
                tag_name += "," + tag.getName();
            }
        }
        List<String> listOfStrings = Arrays.asList(tag_name.split("[ ,]+"));

        List<String> noDuplicatesTags = listOfStrings
                .stream()
                .distinct()
                .collect(Collectors.toList());

        List<Tag> tags = new ArrayList<>();

        for (String _tag : noDuplicatesTags) {
            if (!tagRepository.findByName(_tag).isPresent()) {
                Tag tag = new Tag(_tag);
                tags.add(tag);
                tagRepository.save(tag);
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
        Post post = new Post(postDTO.getId(), authorList(postDTO.getPost_authors()), postDTO.getTitle(), postDTO.getPost_content(), tagList(postDTO.getTag(), postDTO.getId()), postDTO.isIsprivate());
        return post;
    }

    public PostDTO postToPostDTO(Post post)
    {
        PostDTO postDTO = new PostDTO(post.getId(), post.getTitle(), post.getPost_content(), usernamesList(post.getPost_authors()), tagsList(post.getPost_tags()), post.isIsprivate());
                return postDTO;
    }

    public Post addPost(PostDTO postDTO, Principal principal) {

        postDTO.setPost_authors(principal.getName());
        Post post = postDTOtoPost(postDTO);
        Post newPost = postRepository.save(post);

        return newPost;
    }

    public boolean updatePost(Long id, PostDTO postDTO, Principal principal) {

        Optional<Post> post = postRepository.findById(id);

        if(post.isPresent()) {
            if (userService.isAuthor(post.get().getPost_authors(), principal.getName()) || userService.findAdmin(principal)) {
                post = Optional.ofNullable(postDTOtoPost(postDTO));
                postRepository.save(post.get());
                return true;
            }
        }
        return false;
    }

    public boolean deletePost(Long id, Principal principal) throws NotFoundException {

        return Optional.of(postRepository.existsById(id))
                .filter(e -> e)
                .filter(author -> userService.isAuthor(postRepository.getOne(id).getPost_authors(),
                principal.getName()) ||  userService.findAdmin(principal))
                .map(m -> {
                    postRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
         }

    public List<PostDTO> filterPostsByKeyword(String keyword) {

        return getAllPosts()
                .stream()
                .map(this::postToPostDTO)
                .filter(post -> post.getTag().contains(keyword)
                || post.getTitle().contains(keyword)
                || post.getPost_content().contains(keyword))
                .collect(Collectors.toList());
         }

    public boolean addTagToPost(Long id, String tag, Principal principal) throws NotFoundException {

        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException(id.toString()));

         if(principal != null)
         {
            if (userService.isAuthor(post.getPost_authors(), principal.getName()) || userService.findAdmin(principal)) {
                PostDTO postDTO = postToPostDTO(post);
                postDTO.setTag(tag);
                post = postDTOtoPost(postDTO);
                postRepository.save(post);
                return true;
            } else
                return false;
        }
        return false;

    }

}
