package com.randonrainbow.springboot.demo.service;

import com.randonrainbow.springboot.demo.DAO.VideoDAO;
import com.randonrainbow.springboot.demo.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImp implements VideoService{
    public VideoDAO videoDAO;

    @Autowired
    public VideoServiceImp(VideoDAO videoDAO){
        this.videoDAO=videoDAO;
    }

    @Override
    public List<Video> findAll() {
        return videoDAO.findAll();
    }

    @Override
    public Video findById(int id) {
        return videoDAO.findById(id);
    }

    @Override
    public Video save(Video video) {
        return videoDAO.save(video);
    }

    @Override
    public void deleteById(int id) {
        videoDAO.deleteById(id);
    }
}
