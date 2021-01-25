package com.example.blog.controllers;


import com.example.blog.entities.User;
import com.example.blog.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@NoArgsConstructor
@RestController
@RequestMapping("login/api")
public class RestLoginController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody User user) throws RoleNotFoundException {

        if(!userService.getUser(user.getUsername()).isPresent())
         {
           return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User currentUser = userService.getUser(user.getUsername()).get();

        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(currentUser, currentUser.getPassword(), currentUser.getUser_roles());
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new ResponseEntity<User>(HttpStatus.OK);
        }
        catch (NoSuchElementException e)
        {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }

    }
}
