package com.randomrainbow.springboot.demosecurity.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.randomrainbow.springboot.demosecurity.dto.DataUserProfile;
import com.randomrainbow.springboot.demosecurity.dto.UserProfileView;
import com.randomrainbow.springboot.demosecurity.dto.VideoRandomResponse;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.repository.VideoRepository;


import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/api/randomvideo")
public class RandomController {
    private VideoRepository videoRepository;
    private UserRepository userRepository;

    @GetMapping("/server/keep-up")
    public ResponseEntity<String> keepServerAlive() {
        return ResponseEntity.ok("i am breathing, said the server");
    }

    @GetMapping("/{duration}")
    public ResponseEntity<?> getRandomVideo(@PathVariable int duration) {
        Video randomVideo;
        if (duration > 0) {
            // Positive number: treat as max duration (existing behavior)
            randomVideo = videoRepository.getRandomApprovedVideoByDuration(duration);
        } else {
            // Negative number: treat as minimum duration
            randomVideo = videoRepository.getRandomApprovedVideoByMinDuration(Math.abs(duration));
        }
        
        if (randomVideo == null) {
            String message = duration > 0 
                ? "No approved videos found under " + duration + " seconds."
                : "No approved videos found over " + Math.abs(duration) + " seconds.";
            return ResponseEntity.status(404).body(message);
        }
        
        VideoRandomResponse videoResponse = new VideoRandomResponse(
            randomVideo.getVideoLink(), 
            randomVideo.getVideoDescription(), 
            randomVideo.getUser().getUsername(), 
            randomVideo.getTitle(), 
            randomVideo.getEndpoint(),
            randomVideo.getId()
        );
        return ResponseEntity.ok(videoResponse);
    }

    @GetMapping("/video/{token}")
    public ResponseEntity<?> getVideoByToken(@PathVariable String token) {  
        Optional<Video> optionalVideo = videoRepository.findByEndpoint(token);
        if(optionalVideo.isPresent()){
            Video video = optionalVideo.get();
            VideoRandomResponse videoResponse = new VideoRandomResponse(
                video.getVideoLink(), 
                video.getVideoDescription(), 
                video.getUser().getUsername(), 
                video.getTitle(), 
                video.getEndpoint(),
                video.getId()
            );
            return ResponseEntity.ok(videoResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/videosbyartist/{username}")
    public ResponseEntity<?> getVideoByArtist(@PathVariable String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("i am the user " + user);
            DataUserProfile dataUserProfile = new DataUserProfile(user.getArtistDescription(), user.getSocialMedia(), user.getUsername());
            System.out.println("i am think the error is down here");
            List<Video> allVideosApprovedByArtist = videoRepository.findApprovedVideosByUserId(user.getId());
            System.out.println("i am the error");

            UserProfileView userProfileView = new UserProfileView(user.getUsername(), allVideosApprovedByArtist, dataUserProfile);

            return ResponseEntity.ok(userProfileView);
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }
}
