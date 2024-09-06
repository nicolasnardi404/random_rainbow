package com.randomrainbow.springboot.demosecurity.dto;

import com.randomrainbow.springboot.demosecurity.entity.VideoStatus;

public record VideoDTO (int videoId, String title, String videoDescription, String videoLink, VideoStatus videoStatus, String username){
}
