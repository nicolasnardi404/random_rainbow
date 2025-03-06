package com.randomrainbow.springboot.demosecurity.controller;

import com.randomrainbow.springboot.demosecurity.entity.ChatMessage;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.repository.ChatMessageRepository;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatController {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/messages")
    public ResponseEntity<?> addMessage(
            @RequestBody Map<String, String> payload,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

            ChatMessage message = new ChatMessage();
            message.setUser(user);
            message.setContent(payload.get("content"));
            message.setTimestamp(LocalDateTime.now());

            ChatMessage savedMessage = chatMessageRepository.save(message);
            
            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending message: " + e.getMessage());
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages() {
        try {
            return ResponseEntity.ok(chatMessageRepository.findTop50ByOrderByTimestampDesc());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching messages: " + e.getMessage());
        }
    }
} 