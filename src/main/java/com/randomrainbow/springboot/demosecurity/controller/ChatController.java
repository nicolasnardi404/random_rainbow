package com.randomrainbow.springboot.demosecurity.controller;

import com.randomrainbow.springboot.demosecurity.entity.ChatMessage;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.repository.ChatMessageRepository;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/chat")
@AllArgsConstructor
public class ChatController {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/messages")
    public ResponseEntity<?> getMessages() {
        try {
            return ResponseEntity.ok(chatMessageRepository.findTop50ByOrderByTimestampDesc());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching messages: " + e.getMessage());
        }
    }

    @GetMapping("/messages/before/{messageId}")
    public ResponseEntity<?> getMessagesBefore(
            @PathVariable Long messageId,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            PageRequest pageRequest = PageRequest.of(0, limit);
            List<ChatMessage> messages = chatMessageRepository.findByIdLessThanOrderByTimestampDesc(messageId, pageRequest);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching messages: " + e.getMessage());
        }
    }

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
            
            // Broadcast the message to all connected clients
            messagingTemplate.convertAndSend("/topic/messages", savedMessage);
            
            return ResponseEntity.ok(savedMessage);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending message: " + e.getMessage());
        }
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatMessage broadcastMessage(ChatMessage message) {
        return message;
    }
} 