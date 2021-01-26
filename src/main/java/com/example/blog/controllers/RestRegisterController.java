package com.example.blog.controllers;


import com.example.blog.entities.Post;
import com.example.blog.entities.User;
import com.example.blog.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@NoArgsConstructor
@RestController
@RequestMapping("register/api")
public class RestRegisterController {

    @Autowired
    private UserService userService;


    @PostMapping(value = "/register")
    public ResponseEntity<User> createNewUser(@Valid @RequestBody User user, BindingResult bindingResult) throws RoleNotFoundException {

        if (userService.getUser(user.getUsername()).isPresent()) {
            bindingResult.rejectValue("username", "error.username",
                            "Username already in use");
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<User>(HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            userService.saveNewBlogUser(user);
            Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getUser_roles());
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new ResponseEntity<User>(HttpStatus.OK);
        }
        catch (NoSuchElementException e)
        {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

    }
}
