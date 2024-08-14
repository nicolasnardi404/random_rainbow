package com.randomrainbow.springboot.demosecurity.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.randomrainbow.springboot.demosecurity.auth.resetPassword.PasswordResetRequest;
import com.randomrainbow.springboot.demosecurity.entity.Role;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.service.EmailService;
import com.randomrainbow.springboot.demosecurity.service.JwtService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    /**
     * Registers a new user and generates an authentication token.
     *
     * @param request The registration request containing user details.
     * @return AuthenticationResponse containing the generated token.
     */
    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        String token = UUID.randomUUID().toString();
        user.setSimpleToken(token);
        userRepository.save(user);
        emailService.sendVerificationEmail(user, token);

        // Generate both access and refresh tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    /**
     * Authenticates a user and generates an authentication token.
     *
     * @param request The authentication request containing email and password.
     * @return AuthenticationResponse containing the generated token.
     * @throws Exception If the user is not found, email is not verified, or password is incorrect.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!user.isEmailVerified()) {
            throw new Exception("Please verify your email address.");
        }
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Generate both access and refresh tokens
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            return new AuthenticationResponse(accessToken, refreshToken);
        }

        throw new BadCredentialsException("Invalid email or password");
    }

    /**
     * Sends a password reset email with a reset token to the user.
     *
     * @param request The request containing the email of the user requesting password reset.
     * @return AuthenticationResponse indicating success or failure.
     */
    public AuthenticationResponse sendEmailToResetPassword(PasswordResetRequest request) {
        try {
            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String resetToken = UUID.randomUUID().toString();
            user.setSimpleToken(resetToken);
            userRepository.save(user);

            emailService.sendPasswordResetEmail(user, resetToken);
            return new AuthenticationResponse(null);
        } catch (Exception e) {
            log.error("Error sending password reset email", e);
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    /**
     * Updates the user's password using a reset token.
     *
     * @param token       The reset token used to verify the password reset request.
     * @param newPassword The new password to set for the user.
     * @return AuthenticationResponse indicating success or failure.
     */
    public AuthenticationResponse updatePassword(String token, String newPassword) {
        User user = userRepository.findBySimpleToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setSimpleToken(null);
        userRepository.save(user);

        return new AuthenticationResponse(null);
    }

    /**
     * Refreshes the access token using the provided refresh token.
     *
     * @param refreshToken The refresh token to validate and use for generating a new access token.
     * @return AuthenticationResponse containing the new access and refresh tokens.
     */
    public AuthenticationResponse refreshAccessToken(String accessToken) {
        // if (jwtService.isTokenExpired(accessToken)) {
        //     throw new RuntimeException("access token has expired");
        // }
        String username = jwtService.extractUsername(accessToken);
        System.out.println(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
        System.out.println("hello2");
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        System.out.println("hello5");
        // userRepository.save(user);

        return new AuthenticationResponse(newAccessToken, newRefreshToken);
    }
}
