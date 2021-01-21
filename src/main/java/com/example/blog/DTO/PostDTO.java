package com.example.blog.DTO;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;


@Data
public class PostDTO {

    @CsvBindByName
    private Long id;

    private String title;

    @CsvBindByName
    private String tag;

    @CsvBindByName
    private String post_content;

    @CsvBindByName
    private String post_authors;


    private boolean isprivate;

    public boolean isIsprivate() {
        return isprivate;
    }

    public void setIsprivate(boolean isprivate) {
        this.isprivate = isprivate;
    }

    public PostDTO(Long id, String title, String post_content, String post_authors, String tag, boolean isprivate) {

        this.id = id;
        this.title = title;
        this.post_content = post_content;
        this.post_authors = post_authors;
        this.tag = tag;
        this.isprivate = isprivate;
    }


    public PostDTO(){}
}