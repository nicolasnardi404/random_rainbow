package com.randomrainbow.springboot.demosecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.randomrainbow.springboot.demosecurity.auth.JwtAuthentification;
import com.randomrainbow.springboot.demosecurity.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthentification jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

     @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
           .csrf(csrf -> csrf.disable()) // Disable CSRF protection
           .authorizeHttpRequests(authorize -> authorize
               .requestMatchers("/api/v1/auth/**").permitAll() 
               .requestMatchers("/api/v1/demo-controller").permitAll() // Permit all requests to /api/v1/auth/**
               .requestMatchers("/admin/*").hasRole("ROLE_ADMIN")   
               .anyRequest().permitAll()// Require authentication for all other requests
            )
           .sessionManagement(session -> session
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Set session creation policy to STATELESS
            )
           .authenticationProvider(authenticationProvider) // Configure the authentication provider
           .exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"))
           .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add the JWT authentication filter
        
        return http.build();
    }
}
