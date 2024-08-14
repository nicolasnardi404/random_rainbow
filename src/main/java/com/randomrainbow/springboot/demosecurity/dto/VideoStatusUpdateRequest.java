package com.randomrainbow.springboot.demosecurity.dto;

import com.randomrainbow.springboot.demosecurity.entity.VideoStatus;

public record VideoStatusUpdateRequest (int id, VideoStatus videoStatus, String error){
    
}
