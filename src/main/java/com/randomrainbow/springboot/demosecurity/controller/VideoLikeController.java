package com.randomrainbow.springboot.demosecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;

import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.entity.VideoLike;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.repository.VideoRepository;
import com.randomrainbow.springboot.demosecurity.repository.VideoLikeRepository;
import com.randomrainbow.springboot.demosecurity.dto.VideoLikeResponse;
import com.randomrainbow.springboot.demosecurity.service.JwtService;
import com.randomrainbow.springboot.demosecurity.service.VideoLikeService;

@RestController
@RequestMapping("/api/videos")
@AllArgsConstructor
public class VideoLikeController {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final VideoLikeRepository likeRepository;
    private final JwtService jwtService;
    private final VideoLikeService videoLikeService;

    @PostMapping("/{videoId}/like")
    @Transactional
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable int videoId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);
            
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
            Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

            boolean liked = likeRepository.existsByUserAndVideo(user, video);
            
            if (liked) {
                // Unlike
                likeRepository.deleteByUserAndVideo(user, video);
            } else {
                // Like
                VideoLike newLike = new VideoLike();
                newLike.setUser(user);
                newLike.setVideo(video);
                newLike.setCreatedAt(new Date());
                likeRepository.save(newLike);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("liked", !liked);
            response.put("likeCount", likeRepository.countByVideo(video));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

} 
