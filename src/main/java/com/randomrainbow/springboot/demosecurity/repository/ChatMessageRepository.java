package com.randomrainbow.springboot.demosecurity.repository;

import com.randomrainbow.springboot.demosecurity.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop50ByOrderByTimestampDesc();
    
    List<ChatMessage> findByIdLessThanOrderByTimestampDesc(Long messageId, Pageable pageable);
} 
