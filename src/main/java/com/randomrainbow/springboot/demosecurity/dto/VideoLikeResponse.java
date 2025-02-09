package com.randomrainbow.springboot.demosecurity.dto;

public record VideoLikeResponse(
    boolean liked,
    int totalLikes
) {} 