    package com.randomrainbow.springboot.demosecurity.controller;


    import java.util.List;
    import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
    import org.springframework.stereotype.Controller;
    import org.springframework.web.bind.annotation.DeleteMapping;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;

import com.randomrainbow.springboot.demosecurity.dto.UpdateVideo;
import com.randomrainbow.springboot.demosecurity.entity.Video;
    import com.randomrainbow.springboot.demosecurity.repository.VideoRepository;
    import com.randomrainbow.springboot.demosecurity.service.VideoService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

    @AllArgsConstructor
    @Controller
    @RequestMapping("/api/admin")
    public class AdminController {
        private VideoService videoService;
        private VideoRepository videoRepository;

        @GetMapping("/allvideos")
        public ResponseEntity<List<Video>> getAllVideos(){
            List<Video> videos = videoRepository.findAll();
            return ResponseEntity.ok(videos);
        }

        @GetMapping("/review")
        public ResponseEntity<List<Video>> getAllVideosThatNeedReview(){
            List<Video> videos = videoService.getAllVideosThatNeedsReview();
            return ResponseEntity.ok(videos);
        }

    
        @GetMapping("/videos/{id}")
        public ResponseEntity<Video> getVideoById(@PathVariable int id) {
            Optional<Video> videoOptional = videoRepository.findById(id);
            if (videoOptional.isPresent()) {
                return ResponseEntity.ok(videoOptional.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        }

    @PutMapping("/videos/{videoId}")
    public ResponseEntity<Video> updateVideo(@PathVariable("videoId") int videoId, @RequestBody UpdateVideo updatedVideo) {
        try {
            System.out.println("I AM HERE");
            Optional<Video> optionalVideo = videoRepository.findById(videoId);
            if (optionalVideo.isPresent()) {
                Video video = optionalVideo.get();
                video.setTitle(updatedVideo.title());
                video.setVideoDescription(updatedVideo.videoDescription());
                video.setVideoLink(updatedVideo.videoLink());
                videoService.save(video);
                return ResponseEntity.ok(video);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

        @DeleteMapping("/videos/{id}")
        public ResponseEntity<Void> deleteVideo(@PathVariable int id) {
            Optional<Video> videoOptional = videoRepository.findById(id);
            if (videoOptional.isPresent()) {
                videoRepository.delete(videoOptional.get());
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        @PutMapping("/videos/{id}/toggle-approve")
        public ResponseEntity<Video> toggleApproveVideo(@PathVariable int id) {
            Optional<Video> videoOptional = videoRepository.findById(id);
            if (videoOptional.isPresent()) {
                Video video = videoOptional.get();
                video.setApproved(!video.isApproved());
                videoService.save(video);
                return ResponseEntity.ok(video);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }
