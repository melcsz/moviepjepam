package com.example.moviepj.controller;

import com.example.moviepj.persistance.entity.UserEntity;
import com.example.moviepj.service.MailService;
import com.example.moviepj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/email")
public class EmailController {

    private final MailService mailService;
    private final UserService userService;

    @Autowired
    public EmailController(MailService mailService, UserService userService) {
        this.mailService = mailService;
        this.userService = userService;
    }

    @GetMapping("/verify-email")
    @ResponseBody
    public String verifyEmail(@RequestParam("email") String email) throws IOException {
        UserEntity user = userService.getByEmail(email);
        userService.verifyEmail(user);
        return "Verified";
    }

    @PostMapping("/send-subscription-email")
    public String subscribeWithEmail(@ModelAttribute("email") String email) {
        mailService.sendSubscriptionEmail(email);
        return "index";
    }

    @GetMapping("/verify-subscription")
    @ResponseBody
    public String verifySubscription(@RequestParam("email") String email){
        UserEntity user = userService.getByEmail(email);
        userService.verifySubscription(user);
        return "Verified";
    }

}
