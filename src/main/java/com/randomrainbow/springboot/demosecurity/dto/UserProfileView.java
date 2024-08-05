package com.randomrainbow.springboot.demosecurity.dto;

import java.util.List;

import com.randomrainbow.springboot.demosecurity.entity.Video;

public record UserProfileView (String username, List<Video> listVideos, DataUserProfile dataUserProfile) {
    
}
