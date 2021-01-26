package com.example.blog.entities;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;
import java.util.stream.Collectors;


@Data
@Entity
@Table(name = "posts")
public class Post {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_authors",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> post_authors = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_tags",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    private List<Tag> post_tags = new ArrayList<>();


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Length(min = 3, message = "Title must have atleast  " + 3 + " characters")
    @NotEmpty(message = "Title cant be empty")
    private String title;

    @CsvBindByName
    @NotEmpty(message = "Post cant be empty")
    @Column(length=9999)
    private String post_content;

    //private String tag;


    private boolean isprivate;

    public Post(Long id, List<User> post_authors, String title, String post_content, List<Tag> tag, boolean isprivate) {
        this.id = id;
        this.post_authors = post_authors;
        this.title = title;
        this.post_content = post_content;
        this.post_tags = tag;
        this.isprivate = isprivate;
    }

    public Post(){}


    public void addComment(Comment comment)
    {
        comments.add(comment);
    }

    public void removeComment(Comment comment)
    {
        comments.remove(comment);
    }

    public void addAuthor(User user)
    {
        post_authors.add(user);
    }


   @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", post_content='" + post_content + '\'' +
                ", post_authors='" + post_authors.stream().map(User::toString).collect(Collectors.joining(",")) +
                '}';
    }
}
