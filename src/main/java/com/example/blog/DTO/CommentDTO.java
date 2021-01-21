package com.example.blog.DTO;

import lombok.Data;

@Data
public class CommentDTO {

    private Long id;

    private String body;

    private String user;

    private Long post_id;


    public CommentDTO(Long id, String body, String user, Long post_id)
    {
        this.id = id;
        this.body = body;
        this.user = user;
        this.post_id = post_id;
    }

    public CommentDTO(){}
}
