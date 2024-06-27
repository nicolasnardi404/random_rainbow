package com.randomrainbow.springboot.demosecurity.controller;

import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.repository.VideoRepository;
import com.randomrainbow.springboot.demosecurity.service.VideoService;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@Controller
@RequestMapping("/users/{idUser}/videos")
public class VideoController {
    private VideoService videoService;
    private VideoRepository videoRepository;
    private UserRepository userRepository;
  

// STILL NEED TO SET UP TO UPDATE THE DATA, THIS WAY IT JUSTS SENDS U
    @GetMapping("/addNewVideo/{videoId}")
    public ResponseEntity<Video> showUpdateAdd(@PathVariable("idUser") long idUser, @PathVariable("videoId") Optional<Integer> videoId) {
        if (videoId.isPresent()) {
            Video video = videoService.findById(videoId.get());
            System.out.println("Found video with ID: " + videoId.get());
            return ResponseEntity.ok(video);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/addNewVideo")
    public ResponseEntity<Video> showFormAdd(@PathVariable("idUser") long idUser, @RequestBody Video video) {
        
        Optional<User> userOptional = userRepository.findById(idUser);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            Video newVideo = new Video();
            newVideo.setUser(user);
            newVideo.setTitle(video.getTitle());
            newVideo.setVideoDescription(video.getVideoDescription());
            newVideo.setVideoLink(video.getVideoLink());
            newVideo.setApproved(false);
            newVideo.setChecked(false);
            
            return ResponseEntity.ok(videoRepository.save(newVideo));
            
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }

    @DeleteMapping("/deleting/{videoId}")
    public ResponseEntity<String> deleteVideo(@PathVariable("idUser") long idUser, @PathVariable("videoId") int videoId) {
        videoService.deleteById(videoId);
        return ResponseEntity.ok("Video deleted successfully");
    }

}
