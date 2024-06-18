package com.randomrainbow.springboot.demosecurity.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.service.UserService;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/api/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody User user) {
        Optional<User> foundUser = userService.findByUserName(user.getUserName());

        if (foundUser.isPresent() && bCryptPasswordEncoder.matches(user.getPassword(), foundUser.get().getPassword())) {
            System.out.println("hey");
            return ResponseEntity.ok("Authentication successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    // add request mapping for /access-denied
 
    @GetMapping("/access-denied")
    public String showAccessDenied() {

        return "access-denied";
    }

}










