    package com.randomrainbow.springboot.demosecurity.controller;


    import java.util.ArrayList;
    import java.util.Date;
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
import org.springframework.web.bind.annotation.RestController;

import com.randomrainbow.springboot.demosecurity.dto.UpdateVideo;
import com.randomrainbow.springboot.demosecurity.dto.VideoDuration;
import com.randomrainbow.springboot.demosecurity.dto.VideoStatusUpdateRequest;
import com.randomrainbow.springboot.demosecurity.entity.Video;
    import com.randomrainbow.springboot.demosecurity.entity.VideoStatus;
    import com.randomrainbow.springboot.demosecurity.repository.VideoRepository;
    import com.randomrainbow.springboot.demosecurity.service.VideoService;
    import jakarta.transaction.Transactional;
    import lombok.AllArgsConstructor;

    @AllArgsConstructor
    @RestController
    @RequestMapping("/api/admin")
    public class AdminController {
        private VideoService videoService;
        private VideoRepository videoRepository;

        @GetMapping("/allvideos")
        public ResponseEntity<List<Video>> getAllVideos(){
            List<Video> videos = videoRepository.findAll();
            System.out.println("FROM ALL VIDEOS: " + videos);
            return ResponseEntity.ok(videos);
        }

        @GetMapping("/review")
        public ResponseEntity<List<Video>> getAllVideosThatNeedReview(){
            List<Video> videos = videoService.getAllVideosThatNeedsReview();
            System.out.println("FROM ALL REVIEWS: " + videos);
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

        @PutMapping("/videos/status")
        public ResponseEntity<Video> updateVideoStatus(@RequestBody VideoStatusUpdateRequest statusUpdate) {
            Optional<Video> videoOptional = videoRepository.findById(statusUpdate.id());
            if (videoOptional.isPresent()) {
                Video video = videoOptional.get();
                video.setVideoStatus(statusUpdate.videoStatus());
                if (statusUpdate.error() != null) {
                    video.setMessageError(statusUpdate.error());
            }
            videoService.save(video);
            return ResponseEntity.ok(video);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

        @PutMapping("/videos/duration/{id}")
        public ResponseEntity<Video> setVideoDuration(@PathVariable int id, @RequestBody VideoDuration videoDuration) {
            Optional<Video> videoOptional = videoRepository.findById(id);
            if (videoOptional.isPresent()) {
                Video video = videoOptional.get();
                video.setDuration(videoDuration.duration());
                videoService.save(video);
                return ResponseEntity.ok(video);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }
