package com.randomrainbow.springboot.demosecurity.controller;

import com.randomrainbow.springboot.demosecurity.dto.DataUserProfile;
import com.randomrainbow.springboot.demosecurity.dto.VideoDTO;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.entity.VideoStatus;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.repository.VideoRepository;
import com.randomrainbow.springboot.demosecurity.service.VideoService;
import com.randomrainbow.springboot.demosecurity.util.Util;

import lombok.AllArgsConstructor;

import org.aspectj.weaver.patterns.OrPointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController 
@AllArgsConstructor
@RequestMapping("/api/users") 
public class UserController {

    private final VideoService videoService;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    @GetMapping("/{idUser}/videos")
    public ResponseEntity<List<Video>> getUserVideos(@PathVariable("idUser") User idUser) {
        try {
            Optional<List<Video>> listVideos = videoRepository.findAllVideosByIdUser(idUser);
            if(listVideos.isPresent()){
                return ResponseEntity.ok(listVideos.get()); 
            } else{
                return ResponseEntity.notFound().build();  
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
        return ResponseEntity.ok().build(); 
    }
    return ResponseEntity.notFound().build(); 
    }

     @GetMapping("/{idUser}/videos/{videoId}")
    public ResponseEntity<VideoDTO> getVideoById(@PathVariable("idUser") long idUser, @PathVariable("videoId") int videoId) {
        Optional<Video> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            VideoDTO videoDTO = new VideoDTO(video.getTitle(),video.getVideoDescription(), video.getVideoLink());
            return ResponseEntity.ok(videoDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }  

    @PutMapping("/{idUser}/videos/update/{videoId}")
    public ResponseEntity<Video> updateVideo(@PathVariable("videoId") int videoId, @RequestBody Video updatedVideo) {
        try {
            Optional<Video> optionalVideo = videoRepository.findById(videoId);
            if (optionalVideo.isPresent()) {
                Video video = optionalVideo.get();
                video.setTitle(updatedVideo.getTitle());
                video.setVideoDescription(updatedVideo.getVideoDescription());
                videoService.save(video);
                return ResponseEntity.ok(video);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{idUser}/videos/addNewVideo")
    public ResponseEntity<?> showFormAdd(@PathVariable("idUser") int idUser, @RequestBody Video video) {
        Optional<User> userOptional = userRepository.findById(idUser);

        if (userOptional.isPresent()){
            User user = userOptional.get();
            Long existingVideosCount = videoService.countVideoByUserId(idUser);
            if (existingVideosCount >= 3) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
            }

        Optional<Video> videoOptional= videoRepository.findByVideoLink(video.getVideoLink());
        if(videoOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Video Link already used");
        }

            Video newVideo = new Video();
            newVideo.setUser(user);
            newVideo.setTitle(video.getTitle());
            newVideo.setVideoDescription(video.getVideoDescription());
            newVideo.setVideoLink(video.getVideoLink());
            newVideo.setSubmissionDate(new Date());
            newVideo.setActive(true);
            newVideo.setVideoStatus(VideoStatus.UNCHECKED);
            newVideo.setEndpoint(Util.randomString());
            
            System.out.println(newVideo);
            return ResponseEntity.ok(videoRepository.save(newVideo));
            
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

    }

    @DeleteMapping("/{idUser}/videos/delete/{videoId}")
    public ResponseEntity<String> deleteVideo(@PathVariable("idUser") long idUser, @PathVariable("videoId") int videoId) {
        videoService.deleteById(videoId);
        return ResponseEntity.ok("Video deleted successfully");
    }
}
