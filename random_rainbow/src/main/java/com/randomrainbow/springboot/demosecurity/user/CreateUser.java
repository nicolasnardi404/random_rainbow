package com.randomrainbow.springboot.demosecurity.user; // Adjusted package name

import org.springframework.security.crypto.password.PasswordEncoder;

import com.randomrainbow.springboot.demosecurity.auth.RegisterRequest;
import com.randomrainbow.springboot.demosecurity.entity.Role;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service; // Annotate the class to enable dependency injection

@Service // Annotate the class to declare it as a Spring-managed bean
@AllArgsConstructor
public class CreateUser {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User createUser(RegisterRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return user;
    }
}
