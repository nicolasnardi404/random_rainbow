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

import com.randomrainbow.springboot.demosecurity.dto.VideoDTO;
import com.randomrainbow.springboot.demosecurity.dto.VideoDuration;
import com.randomrainbow.springboot.demosecurity.dto.VideoStatusUpdateRequest;
import com.randomrainbow.springboot.demosecurity.dto.VideoWithEndpointDTO;
import com.randomrainbow.springboot.demosecurity.entity.Video;
    import com.randomrainbow.springboot.demosecurity.entity.VideoStatus;
    import com.randomrainbow.springboot.demosecurity.repository.VideoRepository;
import com.randomrainbow.springboot.demosecurity.util.Util;

import jakarta.transaction.Transactional;
    import lombok.AllArgsConstructor;

    @AllArgsConstructor
    @RestController
    @RequestMapping("/api/admin")
    public class AdminController {
        private VideoRepository videoRepository;

        @GetMapping("/allvideos")
        public ResponseEntity<List<VideoWithEndpointDTO>> getAllVideos(){
            List<Video> videos = videoRepository.findAll();

            List<VideoWithEndpointDTO> videoWithEndpointDTO = videoRepository.filterForVideoListWithEndpointDTO(videos);

            return ResponseEntity.ok(videoWithEndpointDTO);
        }

        @GetMapping("/review")
        public ResponseEntity<List<VideoWithEndpointDTO>> getAllVideosThatNeedReview(){
            List<Video> videos = videoRepository.findVideosThatNeedReview();
            List<VideoWithEndpointDTO> videoWithEndpointDTO = videoRepository.filterForVideoListWithEndpointDTO(videos);
            return ResponseEntity.ok(videoWithEndpointDTO);
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
        public ResponseEntity<VideoDTO> updateVideo(@PathVariable("videoId") int videoId, @RequestBody VideoDTO updatedVideo) {
            try {
                Optional<Video> optionalVideo = videoRepository.findById(videoId);
                if (optionalVideo.isPresent()) {
                    Video video = optionalVideo.get();
                    video.setTitle(updatedVideo.title());
                    video.setVideoDescription(updatedVideo.videoDescription());
                    video.setVideoLink(updatedVideo.videoLink());
                    videoRepository.save(video);
                    VideoDTO videoDTO = new VideoDTO(video.getId(), video.getTitle(), video.getVideoDescription(),video.getVideoLink(),video.getVideoStatus(), video.getIdUser().getUsername());

                    return ResponseEntity.ok(videoDTO);
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
                Video video = videoOptional.get();
                videoRepository.delete(video);
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
            videoRepository.save(video);
            return ResponseEntity.ok().build();
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
                videoRepository.save(video);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }
