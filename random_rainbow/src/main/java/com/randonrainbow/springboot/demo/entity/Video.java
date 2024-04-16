package com.randonrainbow.springboot.demo.entity;

import jakarta.persistence.*;

import java.util.Objects;

// if the id will be initialized at the data base how do i define the contructor
@Entity
@Table(name="video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="id_artist")
    private int idArtist;
    @Column(name="title")
    private String title;
    @Column(name="video_description")
    private String videoDescription;
    @Column(name="video_link")
    private String videoLink;
   // private int duration;
    //private boolean checked;
    //private boolean approved;

    public Video(){
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", idArtist=" + idArtist +
                ", title='" + title + '\'' +
                ", videoDescription='" + videoDescription + '\'' +
                ", videoLink='" + videoLink + '\'' +
                '}';
    }

    public Video(int idArtist, String title, String videoDescription, String videoLink) {
        this.idArtist = idArtist;
        this.title = title;
        this.videoDescription = videoDescription;
        this.videoLink = videoLink;
    }


    public String getVideoLink() {
        return videoLink;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public String getTitle() {
        return title;
    }

    public int getIdArtist() {
        return idArtist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
