package com.randomrainbow.springboot.demosecurity.controller;

import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.repository.VideoRepository;
import com.randomrainbow.springboot.demosecurity.service.VideoService;
import com.randomrainbow.springboot.demosecurity.util.Util;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@AllArgsConstructor
@Controller
@RequestMapping("/api/users/{idUser}/videos")
public class VideoController {
    private VideoService videoService;
    private VideoRepository videoRepository;
    private UserRepository userRepository;

    @GetMapping("/{videoId}")
    public ResponseEntity<Video> getVideoById(@PathVariable("idUser") long idUser, @PathVariable("videoId") int videoId) {
        Optional<Video> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isPresent()) {
            return ResponseEntity.ok(videoOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }  

    @PutMapping("/update/{videoId}")
    public ResponseEntity<Video> updateVideo(@PathVariable("videoId") int videoId, @RequestBody Video updatedVideo) {
        try {
            Optional<Video> optionalVideo = videoRepository.findById(videoId);
            if (optionalVideo.isPresent()) {
                Video video = optionalVideo.get();
                video.setTitle(updatedVideo.getTitle());
                video.setVideoDescription(updatedVideo.getVideoDescription());
                video.setVideoLink(updatedVideo.getVideoLink());
                videoService.save(video);
                return ResponseEntity.ok(video);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/addNewVideo")
    public ResponseEntity<Video> showFormAdd(@PathVariable("idUser") int idUser, @RequestBody Video video) {
        
        Optional<User> userOptional = userRepository.findById(idUser);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Long existingVideosCount = videoService.countVideoByUserId(idUser);
            if (existingVideosCount >= 3) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
            }

            Video newVideo = new Video();
            newVideo.setUser(user);
            newVideo.setTitle(video.getTitle());
            newVideo.setVideoDescription(video.getVideoDescription());
            newVideo.setVideoLink(video.getVideoLink());
            newVideo.setApproved(false);
            newVideo.setChecked(false);
            newVideo.setEndpoint(Util.randomString());
            
            return ResponseEntity.ok(videoRepository.save(newVideo));
            
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }

    @DeleteMapping("/delete/{videoId}")
    public ResponseEntity<String> deleteVideo(@PathVariable("idUser") long idUser, @PathVariable("videoId") int videoId) {
        videoService.deleteById(videoId);
        return ResponseEntity.ok("Video deleted successfully");
    }

}
