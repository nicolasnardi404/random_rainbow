package com.randomrainbow.springboot.demosecurity.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.service.EmailService;

import ch.qos.logback.classic.pattern.Util;

import com.randomrainbow.springboot.demosecurity.entity.Role;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.util.*;
import com.randomrainbow.springboot.demosecurity.auth.*;
import com.randomrainbow.springboot.demosecurity.auth.resetPassword.PasswordResetResponse;
import com.randomrainbow.springboot.demosecurity.auth.resetPassword.NewPasswordRequest;
import com.randomrainbow.springboot.demosecurity.auth.resetPassword.PasswordResetRequest;
import com.randomrainbow.springboot.demosecurity.user.CreateUser;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        logger.info("Register request received: {}", request);
        
        User user = createUser(request);
  
        String verificationToken = UUID.randomUUID().toString();
        
        // Save the verification token to the user's record
        user.setVerificationToken(verificationToken);
        userRepository.save(user);
        
        // Send a verification email
        emailService.sendVerificationEmail(user, verificationToken);
        
        return ResponseEntity.ok(new AuthenticationResponse("Registration successful. Please verify your email."));
    }

    private User createUser(RegisterRequest request) {
         User user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        return user;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
            System.out.println(request);
            PasswordResetResponse response = service.sendEmailToResetPassword(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing password reset request");
        }
    }

    @PostMapping("/new-password/{token}")
    public ResponseEntity<?> newPassword(@PathVariable String token, @RequestBody NewPasswordRequest request) {
        try {
            System.out.println(request);
            System.out.println(token);
            service.updatePassword(token, request.newPassword());
            return ResponseEntity.ok("Password successfully updated.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing new password request");
        }
    }
}
