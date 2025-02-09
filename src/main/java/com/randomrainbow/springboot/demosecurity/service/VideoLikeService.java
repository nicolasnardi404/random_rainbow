package com.randomrainbow.springboot.demosecurity.service;

import org.springframework.stereotype.Service;

import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.entity.VideoLike;


import com.randomrainbow.springboot.demosecurity.repository.VideoLikeRepository;

import lombok.AllArgsConstructor;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VideoLikeService {
    private final VideoLikeRepository videoLikeRepository;

    public boolean toggleLike(Video video, User user) {
        Optional<VideoLike> existingLike = videoLikeRepository.findByVideoAndUser(video, user);
        
        if (existingLike.isPresent()) {
            videoLikeRepository.delete(existingLike.get());
            return false; // Now unliked
        } else {
            VideoLike newLike = new VideoLike();
            newLike.setVideo(video);
            newLike.setUser(user);
            videoLikeRepository.save(newLike);
            return true; // Now liked
        }
    }
} 