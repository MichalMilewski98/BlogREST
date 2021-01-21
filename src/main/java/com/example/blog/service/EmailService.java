package com.example.blog.service;

import com.example.blog.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public void sendEmail(User user, String password) throws MailException
    {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setFrom("springblogapplication@gmail.com");
        mail.setSubject("Automatic generated password");
        mail.setText("Your generated password is : " + password);

        javaMailSender.send(mail);
    }

}
