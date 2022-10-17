package com.example.moviepj.service;

import com.example.moviepj.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Component
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Value("${verify.link}")
    private String verifyLink;
    @Value("${verify.subscription}")
    private String verifySubscription;
    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendVerificationEmail(UserDto user)
            throws MessagingException, UnsupportedEncodingException, InterruptedException {


        SimpleMailMessage mailMessage
                = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(user.getEmail());
        mailMessage.setText("Please confirm your email address by clicking this link \n" + verifyLink + user.getEmail());
        mailMessage.setSubject("CONFIRMATION");
        mailSender.send(mailMessage);

    }

    @Async
    public void sendSubscriptionEmail(String email) {
        SimpleMailMessage mailMessage
                = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(email);
        mailMessage.setText("Please confirm your subscription by clicking this link \n" + verifySubscription + email);
        mailMessage.setSubject("SUBSCRIPTION");
        mailSender.send(mailMessage);
    }
}
