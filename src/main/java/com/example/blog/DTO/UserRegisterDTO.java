package com.example.blog.DTO;

import lombok.Data;

@Data
public class UserRegisterDTO {


    private Long id;

    private String email;


    public UserRegisterDTO(Long id, String email)
    {
        this.id = id;
        this.email = email;
    }

    public UserRegisterDTO(){}
}
