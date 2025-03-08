package com.randomrainbow.springboot.demosecurity.controller;

import com.randomrainbow.springboot.demosecurity.auth.AuthenticationResponse;
import com.randomrainbow.springboot.demosecurity.dto.DataUserProfile;
import com.randomrainbow.springboot.demosecurity.dto.VideoDTO;
import com.randomrainbow.springboot.demosecurity.dto.VideoWithEndpointAndErrorDTO;
import com.randomrainbow.springboot.demosecurity.dto.VideoWithEndpointDTO;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import com.randomrainbow.springboot.demosecurity.entity.VideoStatus;
import com.randomrainbow.springboot.demosecurity.repository.UserRepository;
import com.randomrainbow.springboot.demosecurity.repository.VideoRepository;
import com.randomrainbow.springboot.demosecurity.service.EmailService;
import com.randomrainbow.springboot.demosecurity.util.Util;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.AllArgsConstructor;

import org.aspectj.weaver.patterns.OrPointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.randomrainbow.springboot.demosecurity.service.JwtService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    @GetMapping("/{idUser}/videos")
    public ResponseEntity<List<VideoWithEndpointAndErrorDTO>> getUserVideos(@PathVariable("idUser") Long idUser) {
        try {
            Optional<List<Video>> listVideos = videoRepository.findAllVideosByIdUser(idUser);
            if (listVideos.isPresent()) {
            List<Video> list = listVideos.get();

            List<VideoWithEndpointAndErrorDTO> videoDTO = videoRepository.filterForVideoListWithEndpointAndErrorDTO(list);

            return ResponseEntity.ok(videoDTO);
            } else {
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
            return ResponseEntity.ok(new DataUserProfile(user.getArtistDescription(), user.getSocialMedia(), user.getUsername()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update-profile/{idUser}")
    public ResponseEntity<AuthenticationResponse> updateUsername(@PathVariable("idUser") int idUser, @RequestBody DataUserProfile request) {
        try {
            Optional<User> userOptional = userRepository.findById(idUser);
            if (!userOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            User user = userOptional.get();
            user.setUsername(request.username());
            user.setArtistDescription(request.artistDescription());
            user.setSocialMedia(request.socialMedia());
            userRepository.save(user);
            System.out.println(user);
            
            // Generate new tokens using your existing JwtService
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            
            return ResponseEntity.ok(AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @GetMapping("/{idUser}/videos/{videoId}")
    public ResponseEntity<VideoDTO> getVideoById(@PathVariable("idUser") long idUser,
            @PathVariable("videoId") int videoId) {
        Optional<Video> videoOptional = videoRepository.findById(videoId);
        Optional<User> userOptional = userRepository.findById(idUser);
        if (!videoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
            Video video = videoOptional.get();
            User user = userOptional.get();
            VideoDTO videoDTO = new VideoDTO(video.getId(), video.getTitle(), video.getVideoDescription(), video.getVideoLink(), video.getVideoStatus(),user.getUsername());
            return ResponseEntity.ok(videoDTO);
       
    }

    @PutMapping("/{idUser}/videos/update/{videoId}")
    public ResponseEntity<?> updateVideo(@PathVariable("videoId") int videoId, @RequestBody Video updatedVideo) {
        try {
            Optional<Video> optionalVideo = videoRepository.findById(videoId);
            if (optionalVideo.isPresent()) {
                Video video = optionalVideo.get();
                video.setTitle(updatedVideo.getTitle());
                video.setVideoDescription(updatedVideo.getVideoDescription());
                videoRepository.save(video);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{idUser}/videos/addNewVideo")
    public ResponseEntity<?> showFormAdd(@PathVariable("idUser") long idUser, @RequestBody Video video) {
        Optional<User> userOptional = userRepository.findById(idUser);
        System.out.println("numer one");

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Long existingVideosCount = videoRepository.countVideosByUserId(idUser);
    
            if (existingVideosCount >= 3) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
            }

            Optional<Video> videoOptional = videoRepository.findByVideoLink(video.getVideoLink());
            if (videoOptional.isPresent()) {
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

            Video savedVideo = videoRepository.save(newVideo);
            
            // Send email notification to admin
            try {
                emailService.sendNewVideoNotificationToAdmin(user, savedVideo.getTitle(), savedVideo.getEndpoint());
            } catch (Exception e) {
                // Log the error but continue with the video submission process
                System.err.println("Failed to send admin notification email: " + e.getMessage());
            }
            
            return ResponseEntity.ok().build();

        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @DeleteMapping("/{idUser}/videos/delete/{videoId}")
    public ResponseEntity<String> deleteVideo(@PathVariable("idUser") long idUser,
            @PathVariable("videoId") int videoId) {
        videoRepository.deleteById(videoId);
        return ResponseEntity.ok("Video deleted successfully");
    }
}
