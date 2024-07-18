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
import java.util.Random;

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


    @Override
    public Video getRandomApprovedVideoByDuration(int maxDurationSeconds) {
        TypedQuery<Video> query = entityManager.createQuery(
                "SELECT v FROM Video v WHERE v.approved = true AND v.duration <= :maxDuration",
                Video.class);
        query.setParameter("maxDuration", maxDurationSeconds);
        List<Video> approvedVideos = query.getResultList();

        if (approvedVideos.isEmpty()) {
            return null; // Or handle this case appropriately
        }

        Random random = new Random();
        int randomIndex = random.nextInt(approvedVideos.size());
        System.out.println(approvedVideos.size());
        return approvedVideos.get(randomIndex);
}

@Override
public Video getVideoByToken(String endpoint) {
    try {
        TypedQuery<Video> query = entityManager.createQuery("SELECT v FROM Video v WHERE v.endpoint = :endpoint", Video.class);
        query.setParameter("endpoint", endpoint);
        return query.getSingleResult();
    } catch (Exception e) {
        System.out.println("video not found");
        return null;
    }
}


}
