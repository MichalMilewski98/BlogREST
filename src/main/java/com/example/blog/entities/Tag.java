package com.example.blog.entities;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.bytebuddy.asm.Advice;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@AllArgsConstructor
@Table(name = "tags")
public class Tag {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "post_tags")
    private List<Post> posts = new ArrayList<>();

    @Length(min = 3, message = "Tag must have atleast  " + 3 + " characters")
    private String name;

    public Tag(String name)
    {
       this.name = name;
    }

    public Tag(){}


}
