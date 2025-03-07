package com.randomrainbow.springboot.demosecurity.controller;

import com.randomrainbow.springboot.demosecurity.dto.ChatMessageDTO;
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
import java.util.stream.Collectors;

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
            List<ChatMessage> messages = chatMessageRepository.findTop50ByOrderByTimestampDesc();
            List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(ChatMessageDTO::fromEntity)
                .collect(Collectors.toList());
            return ResponseEntity.ok(messageDTOs);
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
            List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(ChatMessageDTO::fromEntity)
                .collect(Collectors.toList());
            return ResponseEntity.ok(messageDTOs);
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
            ChatMessageDTO messageDTO = ChatMessageDTO.fromEntity(savedMessage);
            
            // Broadcast the DTO to all connected clients
            messagingTemplate.convertAndSend("/topic/messages", messageDTO);
            
            return ResponseEntity.ok(messageDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending message: " + e.getMessage());
        }
    }

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteMessage(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);

            ChatMessage message = chatMessageRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Message not found"));

            if (!message.getUser().getUsername().equals(username)) {
                return ResponseEntity.status(403).body("You are not authorized to delete this message.");
            }

            chatMessageRepository.delete(message);

            return ResponseEntity.ok("Message deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting message: " + e.getMessage());
        }
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public ChatMessageDTO broadcastMessage(ChatMessage message) {
        return ChatMessageDTO.fromEntity(message);
    }
} 