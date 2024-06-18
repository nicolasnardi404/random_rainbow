package com.randomrainbow.springboot.demosecurity.controller;

import java.util.Optional;
import java.util.logging.Logger;

import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.service.UserService;
import com.randomrainbow.springboot.demosecurity.user.WebUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private Logger logger = Logger.getLogger(getClass().getName());

    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @PostMapping("/processRegistrationForm")
    public ResponseEntity<?> processRegistrationForm(
            @Valid @RequestBody WebUser theWebUser,
            BindingResult theBindingResult,
            HttpSession session) {

        String userName = theWebUser.getUserName();
        logger.info("Processing registration form for: " + userName);

        // form validation
        if (theBindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid form data");
        }

        // check the database if user already exists
        Optional<User> existing = userService.findByUserName(userName);

        if (existing.isPresent()) {
            logger.warning("User name already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User name already exists.");
        }

        // create user account and store in the database
        userService.save(theWebUser);

        logger.info("Successfully created user: " + userName);

        // place user in the web http session for later use
        session.setAttribute("user", theWebUser);

        return ResponseEntity.ok("Registration successful");
    }
}
