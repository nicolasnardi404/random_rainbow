package com.randomrainbow.springboot.demosecurity.service;

import org.springframework.web.multipart.MultipartFile;

import com.randomrainbow.springboot.demosecurity.entity.User;


public interface EmailService  {
    String sendMail(MultipartFile[] file, String to,String[] cc, String subject, String body);

    void sendVerificationEmail(User user, String token);
}