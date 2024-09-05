package com.randomrainbow.springboot.demosecurity.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;

import jakarta.persistence.NamedQuery;

public interface VideoRepository extends JpaRepository<Video, Integer> {
    Optional<Video> findByVideoLink(String videoLink);
    
    @Query("SELECT v FROM Video v WHERE v.idUser = :idUser")
    Optional<List<Video>> findAllVideosByIdUser(User idUser);

}

