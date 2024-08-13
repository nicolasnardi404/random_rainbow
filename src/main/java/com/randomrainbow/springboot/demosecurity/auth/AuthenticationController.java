package com.randomrainbow.springboot.demosecurity.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.service.EmailService;
import com.randomrainbow.springboot.demosecurity.service.JwtService;
import com.randomrainbow.springboot.demosecurity.service.UserService;

import ch.qos.logback.classic.pattern.Util;

import com.randomrainbow.springboot.demosecurity.entity.Role;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.util.*;
import com.randomrainbow.springboot.demosecurity.auth.*;
import com.randomrainbow.springboot.demosecurity.auth.resetPassword.PasswordResetResponse;
import com.randomrainbow.springboot.demosecurity.dto.TokenRefreshRequest;
import com.randomrainbow.springboot.demosecurity.auth.resetPassword.NewPasswordRequest;
import com.randomrainbow.springboot.demosecurity.auth.resetPassword.PasswordResetRequest;
import com.randomrainbow.springboot.demosecurity.user.CreateUser;

import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        try {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new CustomException("Username already exists");
            }
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new CustomException("Email already exists");
            }
            AuthenticationResponse response = service.register(request);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(AuthenticationResponse.builder().errorMessage(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(AuthenticationResponse.builder().errorMessage("An unexpected error occurred").build());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            AuthenticationResponse response = service.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse(null, e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        try {
            AuthenticationResponse response = service.refreshAccessToken(request.refreshToken());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
            AuthenticationResponse response = service.sendEmailToResetPassword(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing password reset request");
        }
    }

    @PostMapping("/new-password/{token}")
    public ResponseEntity<?> newPassword(@PathVariable String token, @RequestBody NewPasswordRequest request) {
        try {
            service.updatePassword(token, request.newPassword());
            return ResponseEntity.ok("Password successfully updated.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing new password request");
        }
    }
}
