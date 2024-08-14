package com.randomrainbow.springboot.demosecurity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.randomrainbow.springboot.demosecurity.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Integer> {
    Optional<Video> findByVideoLink(String videoLink);

}

