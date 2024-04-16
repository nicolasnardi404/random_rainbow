package com.randonrainbow.springboot.demo.service;

import com.randonrainbow.springboot.demo.entity.Video;

import java.util.List;
/*
* The service implementation is important for make the bridge
* between data sources
* you can make a solicitation to the service
* and it will connect with multiple databases
* instead of making one solicitation to each database
*/

public interface VideoService {
    List<Video> findAll();

    Video findById(int id);

    Video save(Video video);

    void deleteById(int id);
}
