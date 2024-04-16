package com.randonrainbow.springboot.demo.rest;

import com.randonrainbow.springboot.demo.entity.Video;
import com.randonrainbow.springboot.demo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VideoRestController {
    private VideoService videoService;

    @Autowired
    public VideoRestController(VideoService videoService){
        this.videoService=videoService;
    }

    @GetMapping("/videos")
    public List<Video> findAll(){
        return videoService.findAll();
    }
}
