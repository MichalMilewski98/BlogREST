package com.example.blog.service;


import com.example.blog.entities.Post;
import com.example.blog.entities.Tag;
import com.example.blog.repositories.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TagService {

    private TagRepository tagRepository;

    public void insert(Tag tag)
    {
        tagRepository.save(tag);
    }
}
