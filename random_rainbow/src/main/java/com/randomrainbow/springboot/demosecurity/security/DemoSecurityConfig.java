package com.randomrainbow.springboot.demosecurity.security;

import com.randomrainbow.springboot.demosecurity.auth.JwtAuthentification;
import com.randomrainbow.springboot.demosecurity.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class DemoSecurityConfig {

    private final JwtAuthentification jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // bcrypt bean definition
    // @Bean
    // public BCryptPasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }

    // // authenticationProvider bean definition
    // @Bean
    // public DaoAuthenticationProvider authenticationProvider(UserService userService) {
    //     DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
    //     auth.setUserDetailsService(userService); // set the custom user details service
    //     auth.setPasswordEncoder(passwordEncoder()); // set the password encoder - bcrypt
    //     return auth;
    // }

     @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
           .csrf(csrf -> csrf.disable()) // Disable CSRF protection
           .authorizeHttpRequests(authorize -> authorize
               .requestMatchers("/api/v1/auth/**").permitAll() 
               .requestMatchers("/api/v1/demo-controller").permitAll() // Permit all requests to /api/v1/auth/**
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

    // @Bean
    // public SecurityFilterChain filterChain(HttpSecurity http,
    //         AuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {

    //     http
    //         .authorizeHttpRequests(configurer -> configurer
    //             // .requestMatchers("/").hasRole("EMPLOYEE")
    //             // .requestMatchers("/leaders/**").hasRole("MANAGER")
    //             // .requestMatchers("/systems/**").hasRole("ADMIN")
    //             .requestMatchers("/videos/**").permitAll()
    //             .requestMatchers("/api/register/**").permitAll()  // Allow registration API endpoints
    //             // .anyRequest().authenticated())
    //             .anyRequest().permitAll())
    //         .formLogin(form -> form
    //             .loginPage("/showMyLoginPage")
    //             .loginProcessingUrl("/authenticateTheUser")
    //             .successHandler(customAuthenticationSuccessHandler)
    //             .permitAll())
    //         .logout(logout -> logout.permitAll())
    //         .exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied"))
    //         .csrf().disable();  // Disable CSRF protection for simplicity (adjust as needed for production)

    //     return http.build();
    // }

}
