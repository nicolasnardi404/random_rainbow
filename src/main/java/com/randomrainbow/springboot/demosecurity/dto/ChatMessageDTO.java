package com.randomrainbow.springboot.demosecurity.dto;

import com.randomrainbow.springboot.demosecurity.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private Long userId;
    private String username;
    
    public static ChatMessageDTO fromEntity(ChatMessage chatMessage) {
        ChatMessageDTO dto = new ChatMessageDTO();
        dto.setId(chatMessage.getId());
        dto.setContent(chatMessage.getContent());
        dto.setTimestamp(chatMessage.getTimestamp());
        dto.setUserId(chatMessage.getUser().getId());
        dto.setUsername(chatMessage.getUser().getUsername());
        return dto;
    }
} 