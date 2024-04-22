package com.randonrainbow.springboot.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class Security {
    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){

        UserDetails nico = User.builder()
                .username("nico")
                .password("{noop}test123")
                .roles("MANAGER","ADMIN")
                .build();

        UserDetails bea = User.builder()
                .username("bea")
                .password("{noop}test123")
                .roles("MANAGER","ADMIN")
                .build();
        return new InMemoryUserDetailsManager(nico,bea);
    }
}
