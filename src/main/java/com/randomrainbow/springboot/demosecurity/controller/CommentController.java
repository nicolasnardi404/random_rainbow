package com.randomrainbow.springboot.demosecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import com.randomrainbow.springboot.demosecurity.dto.CommentResponse;
import com.randomrainbow.springboot.demosecurity.dto.CommentRequest;
import com.randomrainbow.springboot.demosecurity.entity.Comment;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.repository.CommentRepository;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.repository.VideoRepository;
import com.randomrainbow.springboot.demosecurity.service.JwtService;

import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/videos/{videoId}/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable int videoId) {
        Video video = videoRepository.findById(videoId)
            .orElseThrow(() -> new RuntimeException("Video not found"));

        List<CommentResponse> comments = commentRepository.findByVideoOrderByCreatedAtDesc(video)
            .stream()
            .map(comment -> new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getUsername(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(comments);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable int videoId,
            @RequestBody CommentRequest commentRequest,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
            Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

            Comment comment = new Comment();
            comment.setContent(commentRequest.content());
            comment.setUser(user);
            comment.setVideo(video);
            comment.setCreatedAt(new Date());
            comment.setUpdatedAt(new Date());

            Comment savedComment = commentRepository.save(comment);

            return ResponseEntity.ok(new CommentResponse(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getUser().getUsername(),
                savedComment.getCreatedAt(),
                savedComment.getUpdatedAt()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{commentId}")
    @Transactional
    public ResponseEntity<?> deleteComment(
            @PathVariable int videoId,
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            
            Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

            // Only allow comment deletion by the comment author
            if (!comment.getUser().getUsername().equals(username)) {
                return ResponseEntity.status(403).body("Not authorized to delete this comment");
            }

            commentRepository.delete(comment);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{commentId}")
    @Transactional
    public ResponseEntity<CommentResponse> editComment(
            @PathVariable int videoId,
            @PathVariable Long commentId,
            @RequestBody CommentRequest commentRequest,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            
            Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

            // Only allow comment editing by the comment author
            if (!comment.getUser().getUsername().equals(username)) {
                return ResponseEntity.status(403).body(null);
            }

            comment.setContent(commentRequest.content());
            comment.setUpdatedAt(new Date());
            Comment updatedComment = commentRepository.save(comment);

            return ResponseEntity.ok(new CommentResponse(
                updatedComment.getId(),
                updatedComment.getContent(),
                updatedComment.getUser().getUsername(),
                updatedComment.getCreatedAt(),
                updatedComment.getUpdatedAt()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
} 