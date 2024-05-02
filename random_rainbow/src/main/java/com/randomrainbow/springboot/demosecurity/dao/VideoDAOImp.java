package com.randomrainbow.springboot.demosecurity.dao;

import com.randomrainbow.springboot.demosecurity.entity.User;
import com.randomrainbow.springboot.demosecurity.entity.Video;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/*
Spring @Repository annotation is used to indicate that
the class provides the mechanism
for storage, retrieval, search, update and delete
operation on objects.
*/
@Repository
class VideoDAOImp implements VideoDAO {

    // @Autowired is used for automatic dependency injection
    // in this specific case its necessary because EntityManager is not a bean
    @Autowired
    // the EntityManager is the primary interface for managing entities in JPA
    private EntityManager entityManager;

    @Override
    public List<Video> findAll() {
        // TypedQuery is a query to execute a query against the database
        TypedQuery<Video> theQuery = entityManager.createQuery("FROM Video", Video.class);
        List<Video> videos = theQuery.getResultList();
        return videos;
    }

    @Override
    public List<Video> findVideosByUserId(int userId) {
        // TypedQuery is a query to execute a query against the database
        TypedQuery<Video> theQuery = entityManager
                .createQuery("SELECT v FROM Video v JOIN v.user u WHERE u.id = :idUserVideo", Video.class);
        theQuery.setParameter("idUserVideo", userId); // Corrected parameter name to match the query
        List<Video> videos = theQuery.getResultList();
        return videos;
    }

    @Override
    public List<Video> findVideosByUser(User idUser) {
        TypedQuery<Video> theQuery = entityManager
                .createQuery("SELECT v FROM Video v JOIN v.idUser u WHERE u = :idUserVideo", Video.class);
        theQuery.setParameter("idUserVideo", idUser);

        List<Video> videos = new ArrayList<>();

        try {
            videos = theQuery.getResultList();
        } catch (NoResultException e) {
            System.out.println("No videos found for the user.");
        } catch (Exception e) {
            System.out.println("Error on finding videos: " + e.getMessage());
        }

        return videos;
    }

    @Override
    public Video findById(int id) {
        /*
         * The EntityManager interface in JPA provides methods to manage entities,
         * including persisting, merging, removing, and querying entities.
         * It acts as a bridge between your application and the database,
         * managing the lifecycle of entity instances.
         */
        Video video = entityManager.find(Video.class, id);
        return video;
    }

    @Transactional
    @Override
    public Video save(Video video) {
        Video videoDB = entityManager.merge(video);
        return videoDB;
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        Video video = entityManager.find(Video.class, id);
        entityManager.remove(video);
    }
}
