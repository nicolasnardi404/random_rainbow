package com.randomrainbow.springboot.demosecurity.controller;

import com.randomrainbow.springboot.demosecurity.dto.DataUserProfile;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController 
@RequestMapping("/api/users") 
public class UserController {

    private final VideoService videoService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(VideoService videoService, UserRepository userRepository) {
        this.videoService = videoService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{idUser}/videos")
    public ResponseEntity<List<Video>> getUserVideos(@PathVariable("idUser") User idUser) {
        List<Video> videos = videoService.findVideosByUser(idUser);
        return ResponseEntity.ok(videos); // Returns the list of videos with HTTP 200 OK
    }

    @GetMapping("/profile/{idUser}")
    public ResponseEntity<DataUserProfile> getProfile(@PathVariable("idUser") int idUser) {
        Optional<User> userOptional = userRepository.findById(idUser);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println(user.getArtistDescription());
            System.out.println(user.getSocialMedia());
            return ResponseEntity.ok(new DataUserProfile(user.getArtistDescription(), user.getSocialMedia()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/profile/{idUser}")
    public ResponseEntity<?> updateProfile(@PathVariable("idUser") int idUser, @RequestBody DataUserProfile userProfile) {
    Optional<User> userOptional = userRepository.findById(idUser);
    if (userOptional.isPresent()) {
        User user = userOptional.get();
        user.setArtistDescription(userProfile.artistDescription());
        user.setSocialMedia(userProfile.socialMedia());

        userRepository.save(user);
        return ResponseEntity.ok().build(); // Return OK status after successful update
    }
    return ResponseEntity.notFound().build(); // Return not found if user does not exist
    }

    
}
