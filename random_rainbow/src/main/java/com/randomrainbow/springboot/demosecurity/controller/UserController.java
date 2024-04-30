package com.randomrainbow.springboot.demosecurity.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.service.VideoService;

@Controller
public class UserController {

    @Autowired
    public UserController(VideoService videoService) {
        this.videoService = videoService;
    }

    private VideoService videoService;

    @RequestMapping("/users/{idUser}/videos")
    public String userProfile(@PathVariable("idUser") int idUser, Model theModel) {
        theModel.addAttribute("idUser", idUser);
        System.out.println("THIS IS ID USER");
        System.out.println(idUser);
        List<Video> videos = videoService.findVideosByUser(idUser);
        theModel.addAttribute("videos", videos);

        return "user-interface";
    }

    @GetMapping("/users/{idUser}")
    public RedirectView redirectToNewPath() {
        return new RedirectView("/users/{idUser}/videos");
    }

}
