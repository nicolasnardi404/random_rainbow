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
           .csrf(csrf -> csrf.disable()) 
           .authorizeHttpRequests(authorize -> authorize
               .requestMatchers("/api/admin/**").hasRole("ADMIN")   
               .requestMatchers("/api/users/*").hasAnyRole("ADMIN", "USER")
               .requestMatchers("/api/**").permitAll()
            )
           .sessionManagement(session -> session
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
           .authenticationProvider(authenticationProvider) 
           .exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"))
           .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); 
        
        return http.build();
    }
}
