package com.randomrainbow.springboot.demosecurity.service;

import com.randomrainbow.springboot.demosecurity.entity.User;


import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	UserDetails loadUserByUsername(String userName);

	
}
