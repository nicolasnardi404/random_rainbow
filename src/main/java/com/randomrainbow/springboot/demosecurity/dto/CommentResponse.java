package com.randomrainbow.springboot.demosecurity.dto;

import java.util.Date;

public record CommentResponse(
    Long id,
    String content,
    String username,
    Date createdAt,
    Date updatedAt
) {} 