package com.example.blog.controllers;

import com.example.blog.DTO.PostDTO;
import com.example.blog.entities.Post;
import com.example.blog.entities.User;
import com.example.blog.service.UserService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Controller
public class UploadCSVController {

    @Autowired
    private UserService userService;

    @GetMapping("/upload")
    public String index() {
        return "upload";
    }

    @PostMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {

            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<Post> csvToBean = new CsvToBeanBuilder(reader)
                        .withType(Post.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                for (Post post : (Iterable<Post>) csvToBean) {
                    System.out.println("ID: " + post.getId());
                    System.out.println("Names: " + post.getPost_authors());
                    System.out.println("Content: " + post.getPost_content());
                    //System.out.println("Tags: " + post.getTag());
                }

                // convert `CsvToBean` object to list of users
                List<Post> posts = csvToBean.parse();
                ////////////////////////////////////////////

              /*  for (val post : posts) {
                   List<User> authors = post.getNames();
                    for (val author : authors ) {
                        String username = author.getUsername();
                        User newUser = new User(username);
                        userService.save(newUser);
                        System.out.print(newUser.getUsername());
                    }
                }

               */

                //////////////////////////////////////////////
                // save users list on model
                model.addAttribute("users", posts);
                model.addAttribute("status", true);

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }

        return "upload-status";
    }
}