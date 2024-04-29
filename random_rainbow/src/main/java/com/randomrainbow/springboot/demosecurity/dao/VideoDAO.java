package com.randomrainbow.springboot.demosecurity.dao;

import com.randomrainbow.springboot.demosecurity.entity.Video;

import java.util.List;

/*
* DAO - Data Access Object
* the DAO will store the methods from the app to interact with the database
* in this case the implementation of videoDAO will be only one
* but for good practices its necessary to create first an interface
* and then its class implementation
* that will @Autowired VideoDAO
* */
public interface VideoDAO {

    List<Video> findAll();

    Video findById(int id);

    Video save(Video video);

    void deleteById(int id);

    List<Video> findVideosByUser(int idUser);

}
