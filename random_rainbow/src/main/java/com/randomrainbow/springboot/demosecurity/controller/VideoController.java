package com.randomrainbow.springboot.demosecurity.controller;

import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users/{idUser}/videos")
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

    @GetMapping("/addNewVideo/{videoId}")
    public String showUpdateAdd(@PathVariable("idUser") long idUser, @PathVariable("videoId") Optional<Integer> videoId,
            Model theModel) {
        Video video = null;
        video = videoService.findById(videoId.get());
        System.out.println("Found video with ID: " + videoId.get());
        theModel.addAttribute("video", video);
        theModel.addAttribute("idUser", idUser);
        return "add-form";
    }

    @GetMapping("/addNewVideo")
    public String showFormAdd(@PathVariable("idUser") long idUser, Model theModel) {
        Video video = new Video();
        theModel.addAttribute("video", video);
        theModel.addAttribute("idUser", idUser);
        return "add-form";
    }

    @PostMapping("/save")
    // @ModelAttribute to solicitate the video class
    public String saveEmployee(@PathVariable("idUser") User idUser, @ModelAttribute("video") Video video) {
        video.setUser(idUser);
        videoService.save(video);
        return "redirect:/users/{idUser}/videos";
    }

    @GetMapping("/delete/{videoId}")
    // @RequestParam because you need to solicitate the data
    public String delete(@PathVariable("videoId") int theId) {
        videoService.deleteById(theId);
        return "redirect:/users/{idUser}/videos";
    }

    @GetMapping("/updateVideo")
    public String updateVideo(@RequestParam("userId") long userId, @RequestParam("videoId") int videoId,
            Model theModel) {
        // Assuming you have a method to fetch the video based on videoId
        Video video = videoService.findById(videoId);
        theModel.addAttribute("video", video);
        // Redirect to /addNewVideo including the videoId in the URL
        return "redirect:/users/" + userId + "/videos/addNewVideo/" + videoId;
    }

}
