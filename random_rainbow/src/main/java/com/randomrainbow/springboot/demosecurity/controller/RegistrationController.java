// package com.randomrainbow.springboot.demosecurity.controller;

// import java.util.Optional;
// import java.util.logging.Logger;

// import com.randomrainbow.springboot.demosecurity.entity.User;
// import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
// import com.randomrainbow.springboot.demosecurity.service.UserService;
// import jakarta.servlet.http.HttpSession;
// import jakarta.validation.Valid;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.propertyeditors.StringTrimmerEditor;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.BindingResult;
// import org.springframework.web.bind.WebDataBinder;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/register")
// public class RegistrationController {


//     private UserService userService;

//     private UserRepository repository;

//     @Autowired
//     public RegistrationController(UserService userService, UserRepository repository) {
//         this.userService = userService;
//         this.repository = repository;
//     }



//     @InitBinder
//     public void initBinder(WebDataBinder dataBinder) {
//         StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
//         dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
//     }

//     @PostMapping("/processRegistrationForm")
//     public ResponseEntity<?> processRegistrationForm(
//             @Valid @RequestBody WebUser theWebUser,
//             BindingResult theBindingResult,
//             HttpSession session) {

//         String userName = theWebUser.getUserName();

//         // form validation
//         if (theBindingResult.hasErrors()) {
//             return ResponseEntity.badRequest().body("Invalid form data");
//         }

//         // check the database if user already exists
//         Optional<User> existing = repository.findByUsername(userName);

//         if (existing.isPresent()) {
//             return ResponseEntity.status(HttpStatus.CONFLICT).body("User name already exists.");
//         }

//         // create user account and store in the database
//         userService.save(theWebUser);

//         // place user in the web http session for later use
//         session.setAttribute("user", theWebUser);

//         return ResponseEntity.ok("Registration successful");
//     }
// }
