package com.example.blog.entities;

import com.example.blog.entities.Validation.ValidPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Data
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Unique
    @Column(name = "email", unique = true)
    @Email(message = "Wrong email format")
    private String email;

    @Unique
    @NotEmpty(message = "Username cant be empty")
    @Length(max = 30, message = "Username cant be longer than 30 characters")
    private String username;

    @ValidPassword
    @NotEmpty(message = "Field password cant be empty")
    private String password;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "post_authors")
    private List<Post> posts = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> user_roles = new ArrayList<>();

    public User(String username) {
        this.username = username;
    }

    public void addPost(Post post)
    {
        posts.add(post);
    }

    public User(Long id, String email)
    {
        this.id = id;
        this.email = email;
    }

    public User(){}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean hasRole(String auth) {
        for (Role role : user_roles) {
            if (role.getRole().equals(auth)) { return true; }
        }
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }


    @Override
    public String toString() {
        return "" + username;
    }



  /*  @Override
    public String toString() {
        return "User{" +
//                "id=" + id +
                ", username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                ", posts=" + posts +
//                ", user_roles=" + user_roles +
                '}';
    }

   */

}
