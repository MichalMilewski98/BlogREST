package com.example.blog.repositories;

import com.example.blog.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value="select * from Posts p where p.post_content like %:keyword% or p.title like %:keyword%", nativeQuery = true)
    List<Post> findByKeyword(@Param("keyword") String keyword);

    List<Post> findByOrderByTitleDesc();

    List<Post> findByOrderByTitleAsc();

   // List<Post> findByOrderByPost_contentAsc();

    //List<Post> findByOrderByPost_contentDesc();

}
