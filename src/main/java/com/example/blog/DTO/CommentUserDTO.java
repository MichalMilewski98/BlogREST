package com.example.blog.DTO;

import lombok.Data;

@Data
public class CommentUserDTO {


    private Long id;

    private String body;

    private String user;

    private Long post_id;

    private String username;


    public CommentUserDTO(Long id, String body, String user, Long post_id, String username)
    {
        this.id = id;
        this.body = body;
        this.user = user;
        this.post_id = post_id;
        this.username = username;
    }

    public CommentUserDTO(){}
}
