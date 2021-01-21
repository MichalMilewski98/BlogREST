package com.example.blog.controllers;

import com.example.blog.entities.User;
import com.example.blog.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;

@Log
@AllArgsConstructor
@Controller
@SessionAttributes("user")
public class RegisterController {

    private final UserService userService;

    @GetMapping(value = "/register")
    public String registration(Model model) {

        log.severe("LOGGING");
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping(value = "/register")
    public String createNewUser(@Valid @ModelAttribute User user, BindingResult bindingResult, SessionStatus sessionStatus) throws RoleNotFoundException {
        log.severe("REGISTER");

        if (userService.getUser(user.getUsername()).isPresent()) {
            bindingResult
                    .rejectValue("username", "error.username",
                            "Username already in use");
        }

       if (bindingResult.hasErrors()) {
            System.err.println("New user did not validate");
            return "register";
        }

        userService.saveNewBlogUser(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getUser_roles());
        SecurityContextHolder.getContext().setAuthentication(auth);
        System.err.println("AuthToken: " + auth);  // for testing debugging purposes
        sessionStatus.setComplete();
        System.err.println("SecurityContext Principal: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        log.severe("username : " + user.getUsername());
        log.severe("role : " + user.getUser_roles().get(0).getRole());
        return "redirect:/";

    }
}