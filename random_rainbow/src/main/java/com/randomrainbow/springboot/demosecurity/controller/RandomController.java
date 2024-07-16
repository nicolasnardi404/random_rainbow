package com.randomrainbow.springboot.demosecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.service.VideoService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/api/randomvideo")
public class RandomController {
    private VideoService videoService;

    @GetMapping("/{maxDuration}")
    public ResponseEntity<?> getRandomVideo(@PathVariable int maxDuration) {
        Video randomVideo = videoService.getRandomApprovedVideoByDuration(maxDuration);
        if (randomVideo == null) {
            return ResponseEntity.status(404).body("No approved videos found within the specified duration.");
        }
        return ResponseEntity.ok(randomVideo);
    }
}
