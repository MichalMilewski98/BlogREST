package com.example.blog.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@NoArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Comment cant be empty")
    private String body;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public Comment(Long id, String body, User user, Post post)
    {
        this.id = id;
        this.body = body;
        this.post = post;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", body='" + body + '\'' +
                ", post_id=" + post.getId() +
                '}';
    }

}
