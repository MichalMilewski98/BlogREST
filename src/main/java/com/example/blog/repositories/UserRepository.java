package com.example.blog.repositories;

import com.example.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByUsername(String username);
        @Query(value="select username from Users username = ?0", nativeQuery = true)
        User findUserByUsername(String username);
        Optional<User> findByEmail(String email);

}

