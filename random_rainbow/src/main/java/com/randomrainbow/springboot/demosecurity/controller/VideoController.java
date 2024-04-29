package com.randomrainbow.springboot.demosecurity.controller;

import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.service.UserService;
import com.randomrainbow.springboot.demosecurity.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/videos")
public class VideoController {
    private VideoService videoService;

    @Autowired
    // in this case the @Autowired is not necessary because its only one constructor
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/list")
    public String listVideos(Model theModel) {
        List<Video> videos = videoService.findAll();
        theModel.addAttribute("videos", videos);
        return "list-videos";
    }

    @GetMapping("/profile")
    public String videosUser(@AuthenticationPrincipal User user, Model theModel) {
        int idUser = user.getId();
        List<Video> videos = videoService.findVideosByUser(idUser);
        theModel.addAttribute("videos", videos);
        return "videos-user";
    }

    @GetMapping("/addNewVideo")
    public String showFormAdd(Model theModel) {
        Video video = new Video();
        theModel.addAttribute("video", video);
        return "add-form";
    }

    @PostMapping("/save")
    // @ModelAttribute to solicitate the video class
    public String saveEmployee(@ModelAttribute("video") Video video) {
        videoService.save(video);
        return "redirect:list";
    }

    @GetMapping("/delete")
    // @RequestParam because you need to solicitate the data
    public String delete(@RequestParam("videoId") int theId) {
        videoService.deleteById(theId);
        return "redirect:list";
    }

    @GetMapping("/updateVideo")
    public String updateVideo(@RequestParam("videoId") int theId, Model theModel) {
        Video video = videoService.findById(theId);
        theModel.addAttribute("video", video);
        return "add-form";

    }
}
