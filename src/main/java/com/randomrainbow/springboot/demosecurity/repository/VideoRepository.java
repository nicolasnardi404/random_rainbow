package com.randomrainbow.springboot.demosecurity.repository;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.randomrainbow.springboot.demosecurity.dto.VideoDTO;
import com.randomrainbow.springboot.demosecurity.dto.VideoWithEndpointAndErrorDTO;
import com.randomrainbow.springboot.demosecurity.dto.VideoWithEndpointDTO;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Integer> {
    Optional<Video> findByVideoLink(String videoLink);

    @Query("SELECT v FROM Video v WHERE v.idUser.id = :idUser")
    Optional<List<Video>> findAllVideosByIdUser(long idUser);

    // Get a random approved video by max duration
    @Query("SELECT v FROM Video v WHERE v.videoStatus = com.randomrainbow.springboot.demosecurity.entity.VideoStatus.AVAILABLE AND v.duration <= :maxDuration")
    List<Video> findApprovedVideosByMaxDuration(int maxDuration);

    default Video getRandomApprovedVideoByDuration(int maxDurationSeconds) {
        List<Video> approvedVideos = findApprovedVideosByMaxDuration(maxDurationSeconds);
        if (approvedVideos.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return approvedVideos.get(random.nextInt(approvedVideos.size()));
    }

    public default List<VideoDTO> filterForVideoListDTO(List<Video> list) {
        List<VideoDTO> videoDTOs = list.stream()
                .map(video -> new VideoDTO(
                        video.getId(),
                        video.getTitle(),
                        video.getVideoDescription(),
                        video.getVideoLink(),
                        video.getVideoStatus(),
                        video.getUser().getUsername()))
                .collect(Collectors.toList());
        return videoDTOs;
    }

    public default List<VideoWithEndpointDTO> filterForVideoListWithEndpointDTO(List<Video> list) {
        List<VideoWithEndpointDTO> videoWithEndpointDTO = list.stream()
                .map(video -> new VideoWithEndpointDTO(
                        video.getId(),
                        video.getTitle(),
                        video.getVideoDescription(),
                        video.getVideoLink(),
                        video.getVideoStatus(),
                        video.getEndpoint(),
                        video.getUser().getUsername()))
                .collect(Collectors.toList());
        return videoWithEndpointDTO;
    }

    public default List<VideoWithEndpointAndErrorDTO> filterForVideoListWithEndpointAndErrorDTO(List<Video> list) {
        List<VideoWithEndpointAndErrorDTO> videoWithEndpointDTO = list.stream()
                .map(video -> new VideoWithEndpointAndErrorDTO(
                        video.getId(),
                        video.getTitle(),
                        video.getVideoDescription(),
                        video.getVideoLink(),
                        video.getVideoStatus(),
                        video.getEndpoint(),
                        video.getUser().getUsername(),
                        video.getMessageError()))
                .collect(Collectors.toList());
        return videoWithEndpointDTO;
    }

    // Get video by token (endpoint)
    Optional<Video> findByEndpoint(String endpoint);

    // Get all videos that need review
    @Query("SELECT v FROM Video v WHERE v.videoStatus = com.randomrainbow.springboot.demosecurity.entity.VideoStatus.UNCHECKED")
    List<Video> findVideosThatNeedReview();

    // Count videos by userId
    @Query("SELECT COUNT(v) FROM Video v WHERE v.idUser.id = :userId")
    Long countVideosByUserId(long userId);

    // Get all approved videos by artist (userId)
    @Query("SELECT v FROM Video v WHERE v.idUser.id = :userId AND v.videoStatus = com.randomrainbow.springboot.demosecurity.entity.VideoStatus.AVAILABLE")
    List<Video> findApprovedVideosByUserId(long userId);

}
