package com.randomrainbow.springboot.demosecurity.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;

@RestController
@RequestMapping("/api/verify")
public class VerifyEmail {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<?> verify(@RequestParam("token") String token) {
        Optional<User> userOptional = userRepository.findByVerificationToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setEmailVerified(true); // Mark the email as verified
            userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification failed.");
        }
    }
}