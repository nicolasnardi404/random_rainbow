package com.randomrainbow.springboot.demosecurity.service;

import com.randomrainbow.springboot.demosecurity.dao.VideoDAO;
import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {
    public VideoDAO videoDAO;

    @Autowired
    public VideoService(VideoDAO videoDAO) {
        this.videoDAO = videoDAO;
    }

    public List<Video> findAll() {
        return videoDAO.findAll();
    }

    public Video findById(int id) {
        return videoDAO.findById(id);
    }

    public Video save(Video video) {
        return videoDAO.save(video);
    }

    public void deleteById(int id) {
        videoDAO.deleteById(id);
    }

    public List<Video> findVideosByUser(User idUser) {
        return videoDAO.findVideosByUser(idUser);
    }

    public Video getRandomApprovedVideoByDuration(int maxDuration) {
       return videoDAO.getRandomApprovedVideoByDuration(maxDuration);
    }

    public Video getVideoByToken(String token) {
        return videoDAO.getVideoByToken(token);
    }

    public List<Video> getAllVideos (){
        return videoDAO.getAllVideos();
    }

    public List<Video> getAllVideosThatNeedsReview(){
        return videoDAO.getAllVideosThatNeedsReview();
    }
}
