package com.randomrainbow.springboot.demosecurity.auth;


import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.randomrainbow.springboot.demosecurity.auth.resetPassword.PasswordResetResponse;
import com.randomrainbow.springboot.demosecurity.auth.resetPassword.PasswordResetRequest;
import com.randomrainbow.springboot.demosecurity.entity.Role;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.service.EmailService;
import com.randomrainbow.springboot.demosecurity.service.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        String verificationToken = UUID.randomUUID().toString();

        // Save the verification token to the user's record
        user.setVerificationToken(verificationToken);
        userRepository.save(user);
        emailService.sendVerificationEmail(user, verificationToken);

        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found")); 

        if(!user.isEmailVerified()){
            throw new RuntimeException("User Unabled - Confirm Email Verification");
        }
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user);
            return new AuthenticationResponse(token);
        }

        throw new RuntimeException("Invalid email or password");
    }
    public PasswordResetResponse sendEmailToResetPassword(PasswordResetRequest request) {
    
        try {
            Optional<User> userOptional = userRepository.findByEmail(request.email());
            if (!userOptional.isPresent()) {
                throw new UsernameNotFoundException("User not found");
            }
            User user = userOptional.get();
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            userRepository.save(user);
    
            emailService.sendPasswordResetEmail(user, resetToken);
    
            return new PasswordResetResponse("A password reset link has been sent to your email.");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for further investigation
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    public void updatePassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByVerificationToken(token);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("Invalid reset token");
        }
        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null); // Clear the reset token after successful password reset
        userRepository.save(user);
    }
}
