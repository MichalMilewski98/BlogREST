package com.example.blog.controllers;

import com.example.blog.entities.User;
import com.example.blog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;


    @GetMapping(value = "/users")
    public List<User> users(){
        return userService.getAllUsers();
    }

}
