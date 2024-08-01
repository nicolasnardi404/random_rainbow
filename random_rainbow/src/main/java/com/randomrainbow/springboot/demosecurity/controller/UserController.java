package com.randomrainbow.springboot.demosecurity.controller;

import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController 
@RequestMapping("/api/users") 
public class UserController {

    private final VideoService videoService;

    @Autowired
    public UserController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/{idUser}/videos")
    public ResponseEntity<List<Video>> getUserVideos(@PathVariable("idUser") User idUser) {
        List<Video> videos = videoService.findVideosByUser(idUser);
        return ResponseEntity.ok(videos); // Returns the list of videos with HTTP 200 OK
    }
}
