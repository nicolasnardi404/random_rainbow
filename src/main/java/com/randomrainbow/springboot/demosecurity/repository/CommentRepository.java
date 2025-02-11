package com.randomrainbow.springboot.demosecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.randomrainbow.springboot.demosecurity.entity.Comment;
import com.randomrainbow.springboot.demosecurity.entity.Video;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByVideoOrderByCreatedAtDesc(Video video);
    long countByVideo(Video video);
} 