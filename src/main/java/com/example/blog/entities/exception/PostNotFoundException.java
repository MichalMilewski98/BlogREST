package com.example.blog.entities.exception;

import org.apache.logging.log4j.message.Message;

import java.text.MessageFormat;

public class PostNotFoundException extends RuntimeException{

    public PostNotFoundException(Long id)
    {
        super(MessageFormat.format("Couldn't find post with id: {0}", id));
    }

}
